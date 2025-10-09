package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Party;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PartyService {
    // partijen opslaan
    private List<Party> parties;


    public PartyService() {
        // Hardcoded-data van partijen 2025
        //TODO: ZET EEN LANGER DESCRIPTION!!!!
        parties = new ArrayList<>();
        parties.add(new Party(1L, "PVV",
                "De Partij voor de Vrijheid (PVV), opgericht door Geert Wilders, is een rechts-populistische partij die sterk inzet op thema’s als immigratiebeperking, nationale soevereiniteit en veiligheid. "
                        + "De partij staat kritisch tegenover de Europese Unie, wil strengere grenzen en meer prioriteit voor Nederlandse waarden. "
                        + "Daarnaast pleit de PVV voor lagere belastingen en meer investeringen in zorg en veiligheid.",
                "https://images1.persgroep.net/rcs/bYB1FmwuazV7vEXY4P2KPouYdgc/diocontent/139923768/_fitwidth/763?appId=93a17a8fd81db0de025c8abd1cca1279&quality=0.8"));

        parties.add(new Party(2L, "GROENLINKS / PvdA",
                "GroenLinks en de Partij van de Arbeid (PvdA) vormen sinds 2023 een gezamenlijke fractie onder leiding van Frans Timmermans. "
                        + "Deze samenwerking combineert sociale rechtvaardigheid met klimaatbeleid: een eerlijke verdeling van welvaart, duurzame energie en versterking van het onderwijs. "
                        + "De combinatie streeft naar een sterke verzorgingsstaat en gelijke kansen voor iedereen, met speciale aandacht voor klimaatverandering en mensenrechten.",
                "https://www.pvda.nl/wp-content/uploads/cache/_src76a3dc83068392a10c06822d83c002c6_par55c0ad92b1f69156a79ff6266dd30586_dat1697628464.jpeg"));

        parties.add(new Party(3L, "VVD",
                "De Volkspartij voor Vrijheid en Democratie (VVD) is een liberaal-conservatieve partij die de nadruk legt op economische groei, ondernemerschap en persoonlijke vrijheid. "
                        + "De partij staat voor lage belastingen, een sterke rechtsstaat en een efficiënte overheid. "
                        + "Op sociaal vlak wil de VVD ruimte laten voor individuele keuzes, terwijl ze op veiligheidsgebied streng beleid voert. "
                        + "De partij speelde in de afgelopen decennia vaak een leidende rol in Nederlandse regeringen.",
                "https://www.maxvandaag.nl/wp-content/uploads/2017/03/Logo_VVD.jpg"));

        parties.add(new Party(4L, "Nieuw Sociaal Contract (NSC)",
                "Nieuw Sociaal Contract, opgericht door Pieter Omtzigt, richt zich op bestuurlijke vernieuwing, rechtvaardigheid en transparantie binnen de overheid. "
                        + "De partij wil het vertrouwen in de politiek herstellen door eerlijk bestuur, meer zeggenschap voor burgers en hervorming van het sociale stelsel. "
                        + "NSC benadrukt het belang van solidariteit, menselijke waardigheid en een betrouwbare overheid.",
                "https://storage.googleapis.com/groep-pieter-website/_1200x630_fit_center-center_82_none/NSC_Social_Logo_Navy_Back.png?mtime=1695752534"));

        parties.add(new Party(5L, "D66",
                "Democraten 66 (D66) is een sociaal-liberale partij met nadruk op onderwijs, innovatie, duurzaamheid en Europese samenwerking. "
                        + "De partij pleit voor meer democratische vernieuwing, kansengelijkheid en een open samenleving waarin persoonlijke ontwikkeling centraal staat. "
                        + "D66 ziet een sterke EU als essentieel voor vrede, klimaatbeleid en economische groei.",
                "https://kids.kiddle.co/images/thumb/e/e9/D66_logo_(2019%E2%80%93present).svg/500px-D66_logo_(2019%E2%80%93present).svg.png"));

        parties.add(new Party(6L, "BBB",
                "De BoerBurgerBeweging (BBB), opgericht door Caroline van der Plas, vertegenwoordigt de belangen van het platteland en de agrarische sector. "
                        + "De partij streeft naar meer evenwicht tussen stad en platteland, eerlijke regelgeving voor boeren en bescherming van de voedselproductie in Nederland. "
                        + "BBB pleit ook voor minder bureaucratie en meer respect voor lokale gemeenschappen.",
                "https://www.aaenmaas.nl/publish/pages/2831/bbb-logo.png"));

        parties.add(new Party(7L, "CDA",
                "Het Christen-Democratisch Appèl (CDA) is een middenpartij die christelijke waarden zoals solidariteit, verantwoordelijkheid en gemeenschapszin benadrukt. "
                        + "De partij streeft naar balans tussen economie, milieu en samenleving, en ziet het gezin en lokale gemeenschappen als de kern van de samenleving. "
                        + "Het CDA combineert duurzaamheid met sociale rechtvaardigheid en fatsoenlijk bestuur.",
                "https://www.pw.nl/afbeeldingen/politiek/CDAlogo.jpg/image"));

        parties.add(new Party(8L, "SP",
                "De Socialistische Partij (SP) is een linkse partij die strijdt tegen sociale ongelijkheid en opkomt voor de publieke sector. "
                        + "De SP wil meer investeringen in zorg, onderwijs en betaalbaar wonen, en is kritisch over marktwerking in deze sectoren. "
                        + "De partij benadrukt solidariteit, rechtvaardigheid en menselijke waardigheid.",
                "https://international.sp.nl/sites/international.sp.nl/files/styles/afd_middenkolom/public/sp.png?itok=ggI7SnPB"));

        parties.add(new Party(9L, "DENK",
                "DENK is een partij die zich inzet voor een inclusieve samenleving waarin diversiteit, gelijke kansen en antidiscriminatie centraal staan. "
                        + "De partij wil structureel racisme bestrijden, onderwijs verbeteren en internationale samenwerking bevorderen. "
                        + "Daarnaast vraagt DENK aandacht voor sociale rechtvaardigheid en gelijke behandeling van minderheden.",
                "https://logowik.com/content/uploads/images/denk-20208668.logowik.com.webp"));

        parties.add(new Party(10L, "Partij voor de Dieren",
                "De Partij voor de Dieren (PvdD) plaatst dierenrechten, klimaat en natuur centraal in de politiek. "
                        + "De partij ziet de bescherming van het milieu als voorwaarde voor een rechtvaardige en duurzame samenleving. "
                        + "Ze pleit voor een plantaardiger voedselbeleid, minder consumptie en een economie die niet enkel draait om groei, maar om welzijn.",
                "https://cdn.freebiesupply.com/logos/large/2x/pvdd-logo-png-transparent.png"));

        parties.add(new Party(11L, "Forum voor Democratie",
                "Forum voor Democratie (FvD) is een conservatief-liberale partij onder leiding van Thierry Baudet. "
                        + "De partij benadrukt nationale soevereiniteit, vrijheid van meningsuiting en scepsis tegenover de EU en klimaatbeleid. "
                        + "FvD pleit voor minder bemoeienis van de overheid en behoud van Nederlandse identiteit.",
                "https://www.drentsnieuws.nl/images/nieuws/2019/06/17/fvd.jpg"));

        parties.add(new Party(12L, "SGP",
                "De Staatkundig Gereformeerde Partij (SGP) is de oudste partij van Nederland en baseert haar beleid op Bijbelse waarden. "
                        + "De partij streeft naar een samenleving waarin christelijke normen leidend zijn, met aandacht voor gezin, ethiek en verantwoordelijkheid. "
                        + "De SGP is behoudend op sociaal-ethische thema’s en voorstander van zorgvuldig bestuur.",
                "https://logodix.com/logo/2132331.png"));

        parties.add(new Party(13L, "ChristenUnie",
                "De ChristenUnie combineert christelijke overtuigingen met een sociale en duurzame visie. "
                        + "De partij wil zorg dragen voor kwetsbaren, een gezondere planeet en een eerlijke economie. "
                        + "Ze benadrukt waarden als rentmeesterschap, naastenliefde en balans tussen vrijheid en verantwoordelijkheid.",
                "https://tse4.mm.bing.net/th/id/OIP.uscSAhjcdHlfnjkCAff4HgHaC-?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));

        parties.add(new Party(14L, "Volt",
                "Volt Nederland maakt deel uit van een pan-Europese beweging die gelooft in grensoverschrijdende samenwerking. "
                        + "De partij wil een democratischer, duurzamer en sociaal Europa. "
                        + "Volt pleit voor meer transparantie in de politiek, innovatie in duurzaamheid en versterking van Europese samenwerking.",
                "https://th.bing.com/th/id/R.655284ef6d6972f957dbe88741f6624c?rik=MJfZy4vY6scg2Q&pid=ImgRaw&r=0"));

        parties.add(new Party(15L, "JA21",
                "JA21 is een conservatief-liberale partij die zich richt op streng migratiebeleid, veiligheid en nationale identiteit. "
                        + "De partij is voorstander van lagere belastingen, minder EU-invloed en meer ruimte voor ondernemerschap. "
                        + "Daarnaast wil JA21 de woningmarkt hervormen en meer investeren in defensie.",
                "https://verkiezingen101.nl/wp-content/uploads/2021/02/1200px-JA21_logo.svg_-1024x1024.png"));

        parties.add(new Party(16L, "Vrede voor Dieren",
                "Vrede voor Dieren is een kleine partij die zich richt op dierenrechten, veganisme en een vreedzame samenleving. "
                        + "De partij streeft naar een wereld zonder dierenleed, waarin mens, dier en natuur in balans leven. "
                        + "Ze zet zich in voor onderwijs over compassie en een duurzame voedselketen.",
                "https://samen1.nl/bestanden/uploads/vrede-voor-dieren.png"));

        parties.add(new Party(17L, "BVNL",
                "Belang van Nederland (BVNL), onder leiding van Wybren van Haga, benadrukt vrijheid, veiligheid en economische groei. "
                        + "De partij pleit voor minder overheidsbemoeienis, lagere belastingen en bescherming van ondernemers. "
                        + "Daarnaast zet BVNL zich in voor een streng migratiebeleid en meer politiecapaciteit.",
                "https://tse3.mm.bing.net/th/id/OIP.74bZqC-2w1arcYyICkHVuwHaCI?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));

        parties.add(new Party(18L, "BIJ1",
                "BIJ1 is een progressieve partij die strijdt tegen racisme, seksisme en ongelijkheid. "
                        + "De partij richt zich op radicale gelijkwaardigheid en economische rechtvaardigheid. "
                        + "BIJ1 pleit voor een inclusief beleid waarin alle groepen in de samenleving zich gehoord voelen.",
                "https://www.rug.nl/research/dnpp/politieke-partijen/images/logo-bij1.jpg"));

        parties.add(new Party(19L, "LP",
                "De Libertaire Partij (LP) staat voor maximale individuele vrijheid en minimale staatsinmenging. "
                        + "De partij wil de macht van de overheid beperken, belastingen verlagen en vrije markten stimuleren. "
                        + "Ze benadrukt persoonlijke verantwoordelijkheid en vrijwillige samenwerking boven dwang of regulering.",
                "https://www.rug.nl/research/dnpp/politieke-partijen/lp/logo-lp-2017.png"));

        parties.add(new Party(20L, "50PLUS",
                "50PLUS is een partij die zich inzet voor de belangen van ouderen, met nadruk op pensioenen, zorg en koopkracht. "
                        + "De partij wil een eerlijke verdeling tussen generaties en voldoende aandacht voor senioren op de arbeidsmarkt. "
                        + "Daarnaast streeft 50PLUS naar betaalbare zorg en waardig ouder worden.",
                "https://th.bing.com/th/id/R.61397c44794ba037337bc35ed54bd06d?rik=ievGvFRHNwIkrQ&pid=ImgRaw&r=0"));

        parties.add(new Party(21L, "Piratenpartij",
                "De Piratenpartij is een progressieve beweging die zich inzet voor digitale vrijheid, privacy en transparante overheid. "
                        + "Ze wil burgerrechten beschermen in het digitale tijdperk, open data bevorderen en controle op machtsmisbruik versterken. "
                        + "De partij pleit ook voor hervorming van auteursrecht en vrijheid van informatie.",
                "https://piratenpartij.nl/wp-content/uploads/2013/08/pplogo-st-fixed.png"));

        parties.add(new Party(22L, "FNP",
                "De Friese Nationale Partij (FNP) is een regionale partij die de belangen van Friesland vertegenwoordigt. "
                        + "De partij streeft naar behoud van de Friese taal en cultuur, regionale autonomie en duurzame ontwikkeling in de provincie. "
                        + "FNP richt zich op lokale democratie en evenwichtige groei van stad en platteland.",
                "https://www.rug.nl/research/dnpp/politieke-partijen/fnp/logo-fnp.png"));

        parties.add(new Party(23L, "Vrij Verbond",
                "Het Vrij Verbond is een libertair-geïnspireerde partij die individuele vrijheid en autonomie centraal stelt. "
                        + "De partij verzet zich tegen betutteling, censuur en overmatige overheidscontrole. "
                        + "Ze pleit voor zelfbeschikking, vrijheid van meningsuiting en minimale staatsinmenging.",
                "https://tse3.mm.bing.net/th/id/OIP.qd1VWOLpFgRCqDyTPHH_lQAAAA?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));

        parties.add(new Party(24L, "DE LINIE",
                "DE LINIE is een relatief nieuwe regionale partij die zich richt op lokaal beleid en burgerparticipatie. "
                        + "De partij wil dat bewoners meer invloed krijgen op beslissingen in hun eigen regio en benadrukt transparantie in bestuur. "
                        + "Ze richt zich op duurzaamheid, leefbaarheid en eerlijke verdeling van middelen tussen regio’s.",
                "https://deliniepartij.nl/wp-content/uploads/2025/08/LOGO.png"));

        parties.add(new Party(25L, "NL PLAN",
                "NL PLAN presenteert zich als een toekomstgerichte partij die politiek wil baseren op feiten, data en langetermijnvisie. "
                        + "De partij streeft naar planmatig beleid op het gebied van energie, woningbouw en innovatie, met minder focus op korte-termijnpopulisme. "
                        + "Ze wil een rationelere en transparantere bestuurscultuur bevorderen.",
                "https://irp.cdn-website.com/e843fd98/dms3rep/multi/nlplan_logo_2023-01.png"));

        parties.add(new Party(26L, "ELLECT",
                "ELLECT is een jonge politieke beweging die inzet op vernieuwing, burgerparticipatie en digitale democratie. "
                        + "De partij wil de kloof tussen burger en politiek verkleinen door moderne technologie en transparante besluitvorming. "
                        + "Daarnaast richt ELLECT zich op duurzaamheid, gelijke kansen en innovatieve oplossingen.",
                "https://www.ellect.eu/uploads/YgoQRJ4Q/87x0_87x0/logoklein_356__msi___png.webp"));

        parties.add(new Party(27L, "Partij voor de Rechtsstaat",
                "De Partij voor de Rechtsstaat richt zich op het versterken van de democratische rechtsorde, scheiding der machten en grondrechten. "
                        + "De partij waarschuwt tegen machtsconcentratie en pleit voor eerlijke rechtspraak, onafhankelijke media en bescherming van burgerrechten. "
                        + "Ze ziet de rechtsstaat als fundament van een vrije samenleving.",
                "https://tse1.mm.bing.net/th/id/OIP.4s8hG1Ol4vTPQ9amJsTwFQHaCA?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));
    }
    // partijen ophalen in andere volgorde
    public List<Party> getAllPartiesRandomized() {
        List<Party> shuffled = new ArrayList<>(parties);
        Collections.shuffle(shuffled);
        return shuffled;
    }
    // zoek in de lijst naar de partij met het juiste id
    public Party getPartyById(Long id) {
        return parties.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

