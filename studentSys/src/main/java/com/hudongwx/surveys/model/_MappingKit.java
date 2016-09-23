package com.hudongwx.surveys.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("surveys_t_question_big_type", "id", QuestionBigType.class);
		arp.addMapping("surveys_t_questionnaire", "id", Questionnaire.class);
		arp.addMapping("surveys_t_questionnaire_result", "id", QuestionnaireResult.class);
		arp.addMapping("surveys_t_questions", "id", Questions.class);
		arp.addMapping("surveys_t_questions_questionnaire", "id", QuestionsQuestionnaire.class);
	}
}

