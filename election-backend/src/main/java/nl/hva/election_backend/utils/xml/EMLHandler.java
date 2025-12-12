package nl.hva.election_backend.utils.xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.*;

public class EMLHandler extends DefaultHandler implements TagAndAttributeNames {
    private String currentPartyId = null;
    private static final Set<String> tagsWithoutAttributes = new HashSet<>();
    private static final Map<String, Set<String>> tagsWithAttributes = new HashMap<>();

    static {
        tagsWithoutAttributes.add(RANKING);
        tagsWithoutAttributes.add(ELECTED);
        tagsWithoutAttributes.add(AFFILIATION);
        tagsWithoutAttributes.add(AUTHORITY_ADDRESS);
        tagsWithoutAttributes.add(CANDIDATE);
        tagsWithoutAttributes.add(CANDIDATE_FULL_NAME);
        tagsWithoutAttributes.add(CANDIDATE_LIST);
        tagsWithoutAttributes.add(CAST);
        tagsWithoutAttributes.add(CONTEST);
        tagsWithoutAttributes.add(CONTEST_NAME);
        tagsWithoutAttributes.add(CONTESTS);
        tagsWithoutAttributes.add(COUNT);
        tagsWithoutAttributes.add(COUNTRY_NAME_CODE);
        tagsWithoutAttributes.add(COUNTRY);
        tagsWithoutAttributes.add(CREATION_DATE_TIME);
        tagsWithoutAttributes.add(ELECTION_TREE);
        tagsWithoutAttributes.add(ELECTION);
        tagsWithoutAttributes.add(ELECTION_CATEGORY);
        tagsWithoutAttributes.add(ELECTION_DATE);
        tagsWithoutAttributes.add(ELECTION_EVENT);
        tagsWithoutAttributes.add(ELECTION_NAME);
        tagsWithoutAttributes.add(ELECTION_SUBCATEGORY);
        tagsWithoutAttributes.add(EVENT_IDENTIFIER);
        tagsWithoutAttributes.add(FIRST_NAME);
        tagsWithoutAttributes.add(GENDER);
        tagsWithoutAttributes.add(ISSUE_DATE);
        tagsWithoutAttributes.add(LAST_NAME);
        tagsWithoutAttributes.add(LOCALITY);
        tagsWithoutAttributes.add(LOCALITY_NAME);
        tagsWithoutAttributes.add(MANAGING_AUTHORITY);
        tagsWithoutAttributes.add(MAX_VOTES);
        tagsWithoutAttributes.add(NAME_PREFIX);
        tagsWithoutAttributes.add(NOMINATION_DATE);
        tagsWithoutAttributes.add(NUMBER_OF_SEATS);
        tagsWithoutAttributes.add(PERSON_NAME);
        tagsWithoutAttributes.add(PREFERENCE_THRESHOLD);
        tagsWithoutAttributes.add(REGION);
        tagsWithoutAttributes.add(REGION_NAME);
        tagsWithoutAttributes.add(QUALIFYING_ADDRESS);
        tagsWithoutAttributes.add(REGISTERED_APPELLATION);
        tagsWithoutAttributes.add(REGISTERED_NAME);
        tagsWithoutAttributes.add(REGISTERED_PARTIES);
        tagsWithoutAttributes.add(REGISTERED_PARTY);
        tagsWithoutAttributes.add(REPORTING_UNIT_VOTES);
        tagsWithoutAttributes.add(RESULT);
        tagsWithoutAttributes.add(SELECTION);
        tagsWithoutAttributes.add(TOTAL_VOTES);
        tagsWithoutAttributes.add(TOTAL_COUNTED);
        tagsWithoutAttributes.add(TRANSACTION_ID);
        tagsWithoutAttributes.add(TYPE);
        tagsWithoutAttributes.add(VALID_VOTES);
        tagsWithoutAttributes.add(VOTING_METHOD);

        tagsWithAttributes.put(AFFILIATION_IDENTIFIER, Set.of(ID));
        tagsWithAttributes.put(AUTHORITY_IDENTIFIER, Set.of(ID));
        tagsWithAttributes.put(CANDIDATE_IDENTIFIER, Set.of(SHORT_CODE, ID));
        tagsWithAttributes.put(CANONICALIZATION_METHOD, Set.of(ALGORITHM));
        tagsWithAttributes.put(COMMITTEE, Set.of(ACCEPT_CENTRAL_SUBMISSIONS, COMMITTEE_CATEGORY, COMMITTEE_NAME));
        tagsWithAttributes.put(CONTEST_IDENTIFIER, Set.of(ID));
        tagsWithAttributes.put(CREATED_BY_AUTHORITY, Set.of(ID));
        tagsWithAttributes.put(ELECTION_DOMAIN, Set.of(ID));
        tagsWithAttributes.put(ELECTION_IDENTIFIER, Set.of(ID));
        tagsWithAttributes.put(EML, Set.of(ID, SCHEMA_VERSION, SCHEMA_LOCATION));
        tagsWithAttributes.put(LIST_DATA, Set.of(BELONGS_TO_COMBINATION, BELONGS_TO_SET, PUBLISH_GENDER, PUBLICATION_LANGUAGE));
        tagsWithAttributes.put(NAME_LINE, Set.of(NAME_TYPE));
        tagsWithAttributes.put(REGION, Set.of(REGION_CATEGORY, FRYSIAN_EXPORT_ALLOWED, REGION_NUMBER, REGION_NAME, SUPERIOR_REGION_NUMBER, SUPERIOR_REGION_CATEGORY));
        tagsWithAttributes.put(REJECTED_VOTES, Set.of(REASON_CODE));
        tagsWithAttributes.put(REPORTING_UNIT_IDENTIFIER, Set.of(ID));
        tagsWithAttributes.put(UNCOUNTED_VOTES, Set.of(REASON_CODE));
    }

    private final StringBuilder text = new StringBuilder();
    private Map<String, String> electionData = new HashMap<>();
    private final Deque<Map<String, String>> savedElectionData = new LinkedList<>();

    private boolean aggregated;
    private boolean registerRegion;

    private DefinitionTransformer definitionTransformer;
    private CandidateTransformer candidateTransformer;
    private VotesTransformer votesTransformer;

    public EMLHandler(DefinitionTransformer definitionTransformer) {
        this.definitionTransformer = definitionTransformer;
    }

    public EMLHandler(CandidateTransformer candidateTransformer) {
        this.candidateTransformer = candidateTransformer;
    }

    public EMLHandler(VotesTransformer votesTransformer) {
        this.aggregated = true;
        this.votesTransformer = votesTransformer;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (localName) {
            case REGION:
            case COMMITTEE:
            case REGISTERED_PARTY:
            case AFFILIATION:
                savedElectionData.push(electionData);
                electionData = new HashMap<>(electionData);
                if (REGION.equals(localName)) registerRegion = true;
                break;
            case SELECTION:
            case CANDIDATE:
            case TOTAL_VOTES:
            case REPORTING_UNIT_VOTES:
                savedElectionData.push(electionData);
                electionData = new HashMap<>(electionData);
                break;
        }

        if (tagsWithAttributes.containsKey(localName)) {
            Set<String> knownAttributes = tagsWithAttributes.get(localName);
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrName = attributes.getLocalName(i);
                if (knownAttributes.contains(attrName)) {
                    electionData.put((localName + "-" + attrName).intern(), attributes.getValue(i).intern());
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        text.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        String value = text.toString().trim();
        if (!value.isEmpty() && tagsWithoutAttributes.contains(localName)) {
            electionData.put(localName.intern(), value.intern());
        }
        text.setLength(0);

        if (localName.equals(AFFILIATION_IDENTIFIER)) {
            String foundId = electionData.get(AFFILIATION_IDENTIFIER + "-" + ATTR_ID);
            if (foundId != null) this.currentPartyId = foundId;
        }
        if (localName.equals(AFFILIATION) || localName.equals(REGISTERED_PARTY)) {
            this.currentPartyId = null;
        }

        switch (localName) {
            case REGION:
                if (registerRegion && definitionTransformer != null) definitionTransformer.registerRegion(electionData);
                electionData = savedElectionData.pop();
                break;

            case REGISTERED_PARTY:
                if (definitionTransformer != null) definitionTransformer.registerParty(electionData);
                electionData = savedElectionData.pop();
                break;

            case CANDIDATE:
                if (candidateTransformer != null) {
                    if (this.currentPartyId != null) electionData.put("partyId", this.currentPartyId);
                    candidateTransformer.registerCandidate(electionData);
                }

                if (!savedElectionData.isEmpty()) {
                    String cid = electionData.get(CANDIDATE_IDENTIFIER_ID);
                    if (cid != null) {
                        savedElectionData.peek().put(CANDIDATE_IDENTIFIER_ID, cid);
                    }
                }
                electionData = savedElectionData.pop();
                break;

            case SELECTION:
                if (votesTransformer != null) {
                    if (this.currentPartyId != null) electionData.put("partyId", this.currentPartyId);

                    if (electionData.containsKey(CANDIDATE_IDENTIFIER_ID)) {
                        votesTransformer.registerCandidateVotes(aggregated, electionData);
                    } else {
                        votesTransformer.registerPartyVotes(aggregated, electionData);
                    }
                }
                electionData = savedElectionData.pop();
                break;

            case TOTAL_VOTES:
            case REPORTING_UNIT_VOTES:
                if (votesTransformer != null) votesTransformer.registerMetadata(aggregated, electionData);
                if (TOTAL_VOTES.equals(localName)) aggregated = false;
                electionData = savedElectionData.pop();
                break;

            case AFFILIATION:
                electionData = savedElectionData.pop();
                this.currentPartyId = null;
                break;
        }
    }

    public void setFileName(String fileName) {
        electionData.put("fileName", fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1));
    }
}