package com.hudongwx.studentsys.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hudongwx.studentsys.common.BaseController;
import com.hudongwx.studentsys.model.*;
import com.hudongwx.studentsys.model.Class;
import com.hudongwx.studentsys.service.*;
import com.hudongwx.studentsys.util.Common;
import com.hudongwx.studentsys.util.ObjectKit;
import com.hudongwx.studentsys.util.RenderKit;
import com.hudongwx.studentsys.service.TestQuestionnaireQuestionService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;

import java.util.*;

/**
 * Created by wuhongxu on 2016/10/8 0008.
 */
public class TestController extends BaseController {
    public TestTypeService testTypeService;
    public TestTagService testTagService;
    public TestQuestionService testQuestionService;
    public TestTagQuestionService testTagQuestionService;
    public RoleService roleService;
    public UserService userService;
    public MappingService mappingService;
    public TestQuestionnaireService testQuestionnaireService;
    public TestDomainService testDomainService;
    public TestQuestionnaireQuestionService testQuestionnaireQuestionService;
    public ClassService classService;
    public TestQuestionnaireClassService testQuestionnaireClassService;
    public StudentService studentService;
    public TestReplyService testReplyService;
    public CorrectService correctService;

    /**
     * @return 返回mapping的title属性
     */
    @Override
    public String init() {
        return "我的考试";
    }

    public void history() {
        setMapping(mappingService.getMappingByTitle("考试历史"));
        super.index();
        User user = getCurrentUser(this);
        List<TestQuestionnaire> questionnaires = testQuestionnaireService.getQuestionnairesByUser(user);
        setAttr("testing", questionnaires);
        setAttr("nowTime", System.currentTimeMillis());
    }

    public void testList() {
        setMapping(mappingService.getMappingByUrl("/test/testList"));
        super.index();
        List<TestQuestionnaire> allTestQuestionnaire = testQuestionnaireService.getAllTestQuestionnaire();
        setAttr("questionnaires", allTestQuestionnaire);
        Map<String, User> userMap = new HashMap<>();
        for (TestQuestionnaire tq : allTestQuestionnaire) {
            userMap.put(tq.getId() + "", userService.getUserById(tq.getTestQuestionnaireOperaterId()));
        }
        setAttr("operaterMap", userMap);
        setAttr("classes", classService.getAllClass());

        List<TestQuestionnaireClass> tqcs = testQuestionnaireClassService.getAllTQC();
        Map<String, TestQuestionnaire> testQuestionnaireMap = new HashMap<>();
        Map<String, Class> classMap = new HashMap<>();
        for (TestQuestionnaireClass tqc : tqcs) {
            testQuestionnaireMap.put(tqc.getId() + "", testQuestionnaireService.getQuestionnaireById(tqc.getTestQuestionnaireId()));
            classMap.put(tqc.getId() + "", classService.getClassById(tqc.getClassId()));
        }
        setAttr("disList", tqcs);
        setAttr("testQuestionnaireMap", testQuestionnaireMap);
        setAttr("classMap", classMap);
        /*Map<String,String> msgMap = new HashMap<>();
        for(TestQuestionnaire tq : allTestQuestionnaire){
            testQuestionnaireService.getMsgMapByQuestionnaire(tq);
        }*/
    }

    @Before(POST.class)
    public void addQuestionnaireClass() {
        TestQuestionnaireClass model = getModel(TestQuestionnaireClass.class);
        if (testQuestionnaireClassService._saveTestQuestionnaireClass(model)) {
            RenderKit.renderSuccess(this, "保存成功");
            return;
        }
        RenderKit.renderError(this, "保存失败");
    }

    public void to() {
        setMapping(mappingService.getMappingByTitle("参加考试"));
        super.index();
        User user = getCurrentUser(this);
        List<TestQuestionnaire> questionnaires = testQuestionnaireService.getNowQuestionnaireByUser(user);

        setAttr("testing", questionnaires);
        setAttr("nowTime", System.currentTimeMillis());
    }

    public void questionnaire() {
        fillHeaderAndFooter();
        Integer qcId = getParaToInt(0);
        if (qcId == null) {
            renderError(404);
            return;
        }
        User user = userService.getCurrentUser(this);
        if (null == user) {
            renderError(403);
            return;
        }
        Student student = studentService.getStudentByUser(user);
        if (null == student) {
            renderError(403);
            return;
        }
        log.info(String.format("班级为%s的学生\"%s\"开始了考试,ip为：%s", student.getClassName(), student.getName(), student.getIp()));
        TestQuestionnaire questionnaire = testQuestionnaireService.packingQuestionnaire(qcId);

        if (questionnaire == null) {
            renderError(404);
            return;
        }
        Integer id = questionnaire.getId();
        JSONArray qnArray = JSONArray.parseArray(questionnaire.getTestQuestionnaireTypeList());
        JSONArray typeArray = qnArray.getJSONArray(0);
        JSONArray questionsArray = qnArray.getJSONArray(1);
        Iterator<Object> typeIds = typeArray.iterator();
        String typeStr = ObjectKit.getStrByJSONArray(typeArray);
        List<TestType> types = testTypeService.getTypesByJSONArray(typeArray);
        Map<String, List<TestQuestion>> map = new HashMap<>();
        Map<String, Integer> scoreMap = new HashMap<>();
        int size = 0;
        for (int i = 0; i < types.size(); i++) {
            JSONArray qs = questionsArray.getJSONArray(i);
            List<TestQuestion> testQuestions = testQuestionService.getQuestionsByJSONArray(qs);
            //TODO 这里的写法与数据库交互太频繁
            for (TestQuestion tq : testQuestions) {
                size++;
                TestQuestionnaireQuestion tqq = testQuestionnaireQuestionService.getByQuestionIdAndQuestionnaireId(tq.getId(), id);
                if (tqq != null)
                    scoreMap.put(tq.getId() + "", tqq.getTestQuestionScore());
                else
                    scoreMap.put(tq.getId() + "", tq.getTestQuestionDefaultScore());
            }
            map.put(types.get(i).getId() + "", testQuestions);
        }
        TestReply testReply = testReplyService.getByCache(questionnaire.getTestQuestionnaireClassId(), student.getId());
        setAttr("testReply", JsonKit.toJson(testReply));
        setAttr("types", types);
        setAttr("scoreMap", scoreMap);
        setAttr("questionMap", map);
        setAttr("questionnaire", questionnaire);
        setAttr("questionSize", size);
        setAttr("student", student);
        render("/test/questionnaire.ftl");
    }

    public void showCorrecting() {
        fillHeaderAndFooter();
        Integer qcId = getParaToInt(0);
        Integer studentId = getParaToInt(1);
        if (qcId == null) {
            renderError(404);
            return;
        }
        User user = userService.getCurrentUser(this);
        if (null == user) {
            renderError(403);
            return;
        }
        Student student = studentService.getStudentById(studentId);
        if (null == student) {
            renderError(403);
            return;
        }
        TestQuestionnaire questionnaire = testQuestionnaireService.packingQuestionnaire(qcId);

        if (questionnaire == null) {
            renderError(404);
            return;
        }
        Integer id = questionnaire.getId();
        JSONArray qnArray = JSONArray.parseArray(questionnaire.getTestQuestionnaireTypeList());
        JSONArray typeArray = qnArray.getJSONArray(0);
        JSONArray questionsArray = qnArray.getJSONArray(1);
        Iterator<Object> typeIds = typeArray.iterator();
        String typeStr = ObjectKit.getStrByJSONArray(typeArray);
        List<TestType> types = testTypeService.getTypesByJSONArray(typeArray);
        Map<String, List<TestQuestion>> map = new HashMap<>();
        Map<String, Integer> scoreMap = new HashMap<>();
        int size = 0;
        for (int i = 0; i < types.size(); i++) {
            JSONArray qs = questionsArray.getJSONArray(i);
            List<TestQuestion> testQuestions = testQuestionService.getQuestionsByJSONArray(qs);
            //TODO 这里的写法与数据库交互太频繁
            for (TestQuestion tq : testQuestions) {
                size++;
                //你问我为什么要在这里写这一句？你删掉就知道了=。=
                boolean x = true;
                TestQuestionnaireQuestion tqq = testQuestionnaireQuestionService.getByQuestionIdAndQuestionnaireId(tq.getId(), id);
                if (tqq != null)
                    scoreMap.put(tq.getId() + "", tqq.getTestQuestionScore());
                else
                    scoreMap.put(tq.getId() + "", tq.getTestQuestionDefaultScore());
            }
            map.put(types.get(i).getId() + "", testQuestions);
        }
        TestReply testReply = testReplyService.getReply(questionnaire.getTestQuestionnaireClassId(), student.getId(), false);
        setAttr("testReply", JsonKit.toJson(testReply));
        setAttr("types", types);
        setAttr("scoreMap", scoreMap);
        setAttr("questionMap", map);
        setAttr("questionnaire", questionnaire);
        setAttr("questionSize", size);
        setAttr("student", student);
        render("/test/showCorrecting.ftl");
    }

    @Before(POST.class)
    public void cacheAnswer() {
        TestReply reply = new TestReply();
        reply.setAnswers(getPara("answers"));
        reply.setStudentId(getParaToInt("studentId"));
        reply.setTestQuestionnaireClassId(getParaToInt("testQuestionnaireClassId"));
        reply.setId(getParaToInt("id"));
        if (reply.getAnswers() == null || reply.getTestQuestionnaireClassId() == null || reply.getStudentId() == null) {
            RenderKit.renderError(this, "你的信息出现了错误,这将导致你不能提交答案，请刷新重试，答案会自动回滚到上次自动保存的时间点");
            return;
        }
        testReplyService.putByCache(reply);
        RenderKit.renderSuccess(this);
    }

    @Before(POST.class)
    public void postScore() {
        Integer replyId = getParaToInt(0);
        if (replyId == null) {
            RenderKit.renderError(this);
            return;
        }
        TestReply tp = testReplyService.getReplyById(replyId);
        if (tp == null) {
            RenderKit.renderError(this);
            return;
        }
        tp.setScoreSituation(getPara("scoreSituation"));
        tp.setScore(getParaToInt("score"));
        tp.setCorrecting(Common.VALUE_VISIBLE);
        if (testReplyService._updateTestReply(tp)) {
            RenderKit.renderSuccess(this, "提交成功");
            return;
        }
        RenderKit.renderError(this, "提交失败");
    }

    @Before(POST.class)
    public void postReply() {
        long now = System.currentTimeMillis();
        TestReply reply = new TestReply();
        reply.setAnswers(getPara("answers"));
        reply.setStudentId(getParaToInt("studentId"));
        reply.setTestQuestionnaireClassId(getParaToInt("testQuestionnaireClassId"));
        reply.setId(getParaToInt("id"));
        if (reply.getAnswers() == null || reply.getTestQuestionnaireClassId() == null || reply.getStudentId() == null) {
            log.info(reply.getAnswers());
            RenderKit.renderError(this, "你的信息出现了错误,这将导致你不能提交答案，请刷新重试，答案会自动回滚到上次自动保存的时间点");
            return;
        }
        Integer qcId = reply.getTestQuestionnaireClassId();
        TestQuestionnaireClass tqc = testQuestionnaireClassService.getById(qcId);
        if (tqc == null) {
            RenderKit.renderError(this, "获取试卷信息失败");
            return;
        }
        if (tqc.getTestQuestionnaireStartTime() > now || tqc.getTestQuestionnaireEndTime() < now) {
            RenderKit.renderError(this, "你已超时，无法提交！");
            return;
        }
        if (testReplyService.putByCache(reply, true).getId() == null) {
            RenderKit.renderError(this, "保存失败");
            return;
        }
        correctService.correct(reply);
        RenderKit.renderSuccess(this, "保存成功");
    }

    public void questions() {
        setMapping(mappingService.getMappingByTitle("题库"));
        super.index();
        List<TestType> testTypes = testTypeService.getAllVisibleTypes();
        setAttr("types", testTypes);
        Map<String, TestType> testTypeMap = new HashMap<>();
        for (TestType t : testTypes) {
            testTypeMap.put(t.getId() + "", t);
        }
        setAttr("testTypeMap", testTypeMap);
        List<TestTag> tags = testTagService.getAllTestTag();
        setAttr("tags", tags);

        List<TestQuestion> allQuestions = testQuestionService.getAllQuestions();
        setAttr("questions", allQuestions);
        Map<String, List<TestTag>> testQuestionTagsMap = new HashMap<>();
        for (TestQuestion tq : allQuestions) {
            testQuestionTagsMap.put(tq.getId() + "", testTagService.getTagsByQuestion(tq));
        }
        setAttr("testQuestionTags", testQuestionTagsMap);
        //能够进行添加题目的角色
        Mapping mapping = mappingService.getMappingByUrl("addTestQuestion");
        List<Role> roles = roleService.getRoleByMapping(mapping);
        Map<String, User> userMap = new HashMap<>();
        List<User> users = new ArrayList<>();
        for (Role role : roles) {
            users.addAll(userService.getUsersByRole(role));
        }
        for (User user : users) {
            userMap.put(user.getId() + "", user);
        }
        setAttr("userMap", userMap);
    }

    public void delayTest() {
        Integer qcId = getParaToInt(0);
        TestQuestionnaireClass tqc = testQuestionnaireClassService.getById(qcId);
        if (tqc == null) {
            RenderKit.renderError(this, "未找到試卷");
            return;
        }
        Long temp = tqc.getTestQuestionnaireTempTime();
        tqc.setTestQuestionnaireTempTime(tqc.getTestQuestionnaireEndTime());
        tqc.setTestQuestionnaireEndTime(temp);
        tqc.update();
        RenderKit.renderSuccess(this, "成功");
    }

    public void closeTest() {
        delayTest();
    }

    public void count() {
        setMapping(mappingService.getMappingByUrl("/test/count"));
        super.index();
        Integer classId = getParaToInt(0);
        List<Class> classes = classService.getAllClass();
        List<TestQuestionnaire> questionnaires = new ArrayList<>();
        Class ac = classService.getClassById(classId);
        if (classes.size() > 0 && ac == null) {
            ac = classes.get(0);
        }
        if (ac != null) {
            questionnaires = testQuestionnaireService.getQuestionnairesByClass(ac);
        }
        setAttr("nowTime", System.currentTimeMillis());
        setAttr("questionnaires", questionnaires);
        setAttr("classes", classes);
        setAttr("nowClass", ac);
    }

    public void getResults() {
        Integer qcId = getParaToInt(0);
        TestQuestionnaireClass tqc = testQuestionnaireClassService.getById(qcId);
        List<TestReply> replies = testReplyService.getReplies(qcId);
        Map<String, Student> studentMap = new HashMap<>();
        for (TestReply reply : replies) {
            studentMap.put(reply.getId() + "", studentService.getStudentById(reply.getStudentId()));
        }
        setAttr("studentMap", studentMap);
        setAttr("replies", replies);
        setAttr("testQuestionnaireClass", tqc);
        render("/test/results.ftl");
    }

    public void selectQuestions() {
        List<TestType> allTestTypes = testTypeService.getAllVisibleTypes();
        setAttr("types", allTestTypes);
        List<Domain> allDomains = testDomainService.getAllDomains();
        setAttr("domains", allDomains);

        //取得第一个domain下面的tag
        List<TestTag> tags = testTagService.getTagsByDomain(allDomains.get(0));
        setAttr("tags", tags);
        //questions();
        render("/test/selectQuestion.ftl");
    }

    public void preview() {
        fillHeaderAndFooter();
        String qn = getPara("questions");
        Integer id = getParaToInt("id");
        boolean isHave = (id != null);
        JSONArray qnArray = JSONArray.parseArray(qn);
        JSONArray typeArray = qnArray.getJSONArray(0);
        JSONArray questionsArray = qnArray.getJSONArray(1);
        Iterator<Object> typeIds = typeArray.iterator();
        String typeStr = ObjectKit.getStrByJSONArray(typeArray);
        List<TestType> types = testTypeService.getTypesByJSONArray(typeArray);
        Map<String, List<TestQuestion>> map = new HashMap<>();
        Map<String, Integer> scoreMap = new HashMap<>();
        for (int i = 0; i < types.size(); i++) {
            JSONArray qs = questionsArray.getJSONArray(i);
            List<TestQuestion> testQuestions = testQuestionService.getQuestionsByJSONArray(qs);
            if (isHave) {
                //TODO 这里的写法与数据库交互太频繁
                for (TestQuestion tq : testQuestions) {
                    TestQuestionnaireQuestion tqq = testQuestionnaireQuestionService.getByQuestionIdAndQuestionnaireId(tq.getId(), id);
                    if (tqq != null)
                        scoreMap.put(tq.getId() + "", tqq.getTestQuestionScore());
                }
            }
            map.put(types.get(i).getId() + "", testQuestions);
        }
        setAttr("types", types);
        setAttr("scoreMap", scoreMap);
        setAttr("questionMap", map);
        render("/test/preview.ftl");
    }

    @Before(POST.class)
    public void getTags() {
        Integer domainId = getParaToInt("domain");
        if (domainId == null) {
            RenderKit.renderError(this, "参数不对");
            return;
        }
        Domain domain = testDomainService.getDomainById(domainId);
        if (domain == null) {
            RenderKit.renderError(this, "没有该分类");
            return;
        }
        List<TestTag> tags = testTagService.getTagsByDomain(domain);
        JSONArray array = new JSONArray();
        for (TestTag tag : tags) {
            JSONObject json = new JSONObject();
            json.put("id", tag.getId());
            json.put("tagName", tag.getTagName());
            array.add(json);
        }
        renderJson(array);
    }

    @Before(POST.class)
    public void getQuestions() {
        Integer tagId = getParaToInt("tag");
        Integer typeId = getParaToInt("type");
        List<TestQuestion> questions = testQuestionService.getQuestionsByTypeIdAndTagId(tagId, typeId);
        JSONArray array = new JSONArray();
        for (TestQuestion q : questions) {
            JSONObject json = new JSONObject();
            json.put("id", q.getId());
            json.put("title", q.getTestQuestionTitle());
            json.put("type", q.getTestQuestionTypeId());
            array.add(json);
        }
        RenderKit.renderSuccess(this, array);
    }

    @Before(POST.class)
    public void addTestQuestionnaire() {
        TestQuestionnaire model = getModel(TestQuestionnaire.class);
        boolean flag;
        if (model.getId() == null)
            flag = model.save();
        else {
            flag = model.update();
            testQuestionnaireQuestionService._deleteAllByQuestionnaire(model);
        }

        if (!flag) {
            RenderKit.renderError(this, "保存试卷失败");
            return;
        }
        String para = getPara("questionnaireQuestions");
        JSONArray array = JSONArray.parseArray(para);
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Integer questionId = obj.getInteger("testQuestionId");
            Integer testQuestionScore = obj.getInteger("testQuestionScore");
            TestQuestionnaireQuestion tqq = new TestQuestionnaireQuestion();
            tqq.setTestQuestionnaireId(model.getId());
            tqq.setTestQuestionId(questionId);
            tqq.setTestQuestionScore(testQuestionScore);
            tqq.setTestQuestionIsVisible(Common.VALUE_VISIBLE);
            if (!testQuestionnaireQuestionService._saveTestQuestionnaireQuestion(tqq)) {
                RenderKit.renderError(this, "关联试题失败！");
                return;
            }
        }
        RenderKit.renderSuccess(this, "添加成功");
    }

    @Before(POST.class)
    public void addTestQuestion() {
        TestQuestion model = getModel(TestQuestion.class);
        boolean b;
        if (null == model.getId())
            b = testQuestionService._saveTestQuestion(model);
        else
            b = testQuestionService._updateTestQuestion(model);
        String tagsPara = getPara("tags");
        String[] tags = tagsPara.split(",");
        List<TestTag> allTestTag = testTagService.getAllTestTag();
        for (String tag : tags) {
            TestTag now = null;
            for (TestTag t : allTestTag) {
                if (t.getTagName().equals(tag)) {
                    now = t;
                }
            }
            if (now == null) {
                now = new TestTag();
                now.setTagName(tag);
                now.setQuestionCnt(0);
                testTagService._saveTestTag(now);
            }
            now.setQuestionCnt(now.getQuestionCnt() + 1);
            testTagService._updateTestTag(now);
            testTagQuestionService._saveTagQuestion(now, model);
        }
        if (b) {
            RenderKit.renderSuccess(this, "保存成功");
            return;
        }
        RenderKit.renderError(this, "保存失败");
    }

    @Before(POST.class)
    public void deleteTestQuestion() {
        Integer id = getParaToInt(0);
        if (id == null) {
            RenderKit.renderError(this, "该问题不存在或已被删除");
            return;
        }
        TestQuestion tq = testQuestionService.getQuestionById(id);
        if (tq == null) {
            RenderKit.renderError(this, "该问题不存在或已被删除");
            return;
        }
        tq.delete();
        RenderKit.renderSuccess(this, "删除成功");
    }

    @Before(POST.class)
    public void deleteQuestionnaire() {
        Integer id = getParaToInt(0);
        if (id == null) {
            RenderKit.renderError(this, "该试卷不存在或已被删除");
            return;
        }
        TestQuestionnaire tq = testQuestionnaireService.getQuestionnaireById(id);
        if (tq == null) {
            RenderKit.renderError(this, "该班级不存在或已被删除");
            return;
        }
        tq.delete();
        RenderKit.renderSuccess(this, "删除成功");
    }
}
