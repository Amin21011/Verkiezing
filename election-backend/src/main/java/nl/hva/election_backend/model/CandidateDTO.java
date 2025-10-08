package nl.hva.election_backend.model;

public class CandidateDTO {

    private int id;              // bv. "1"
    private String firstName;       // bv. "Dilan"
    private String lastName;        // bv. "Yeşilgöz"
    private int votes; // <- toegevoegd voor telling-bestanden

    public CandidateDTO(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.votes = 0;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public String getName() { return firstName +  " " + lastName; }


    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }



    @Override
    public String toString() {
        return String.format("%s %s - Aantal stemmen: %d", firstName, lastName, votes);
    }
}
