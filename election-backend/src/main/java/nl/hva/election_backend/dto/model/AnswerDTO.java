package nl.hva.election_backend.dto.model;

public class AnswerDTO {
    private long questionId;
    private String answer;

    public AnswerDTO() {}

    public AnswerDTO(long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public long getQuestionId() { return questionId; }
    public String getAnswer() { return answer; }
    public void setQuestionId(long questionId) { this.questionId = questionId; }
    public void setAnswer(String answer) { this.answer = answer; }
}

