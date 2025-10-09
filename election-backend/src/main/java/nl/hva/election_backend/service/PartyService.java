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
        parties.add(new Party(1L, "PVV", "Partij voor de Vrijheid — rechts-populistische partij, nadruk op immigratie, veiligheid en soevereiniteit.", "https://images1.persgroep.net/rcs/bYB1FmwuazV7vEXY4P2KPouYdgc/diocontent/139923768/_fitwidth/763?appId=93a17a8fd81db0de025c8abd1cca1279&quality=0.8"));
        parties.add(new Party(2L, "GROENLINKS / PvdA", "Samenwerking tussen GroenLinks en PvdA — progressief, aandacht voor milieu en sociale gelijkheid.", "https://www.pvda.nl/wp-content/uploads/cache/_src76a3dc83068392a10c06822d83c002c6_par55c0ad92b1f69156a79ff6266dd30586_dat1697628464.jpeg"));
        parties.add(new Party(3L, "VVD", "Volkspartij voor Vrijheid en Democratie — liberaal-conservatief, focus op economie en individuele vrijheid.", "https://www.maxvandaag.nl/wp-content/uploads/2017/03/Logo_VVD.jpg"));
        parties.add(new Party(4L, "Nieuw Sociaal Contract (NSC)", "Nieuw Sociaal Contract — gericht op een sociaal-maatschappelijk pact en hervormingen.", "https://storage.googleapis.com/groep-pieter-website/_1200x630_fit_center-center_82_none/NSC_Social_Logo_Navy_Back.png?mtime=1695752534"));
        parties.add(new Party(5L, "D66", "Democraten 66 — liberaal-progressief met nadruk op onderwijs, innovatie en Europese samenwerking.", "https://kids.kiddle.co/images/thumb/e/e9/D66_logo_(2019%E2%80%93present).svg/500px-D66_logo_(2019%E2%80%93present).svg.png"));
        parties.add(new Party(6L, "BBB", "BoerBurgerBeweging — belangen van de plattelandsgemeenschappen en agrarische sector centraal.", "https://www.aaenmaas.nl/publish/pages/2831/bbb-logo.png"));
        parties.add(new Party(7L, "CDA", "Christen-Democratisch Appèl — middenpartij met focus op gemeenschapszin, gezin en duurzaamheid.", "https://www.pw.nl/afbeeldingen/politiek/CDAlogo.jpg/image"));
        parties.add(new Party(8L, "SP", "Socialistische Partij — linkse partij, nadruk op ongelijkheid, sociale voorzieningen en publieke sector.", "https://international.sp.nl/sites/international.sp.nl/files/styles/afd_middenkolom/public/sp.png?itok=ggI7SnPB"));
        parties.add(new Party(9L, "DENK", "DENK — politiek geluid voor diversiteit, integratie en gelijke kansen.", "https://logowik.com/content/uploads/images/denk-20208668.logowik.com.webp"));
        parties.add(new Party(10L, "Partij voor de Dieren", "Partij voor de Dieren — dierenrechten, natuur en milieu centraal in politiek beleid.", "https://cdn.freebiesupply.com/logos/large/2x/pvdd-logo-png-transparent.png"));
        parties.add(new Party(11L, "Forum voor Democratie", "Forum voor Democratie — conservatief-liberale partij met nadruk op nationale soevereiniteit.", "https://www.drentsnieuws.nl/images/nieuws/2019/06/17/fvd.jpg"));
        parties.add(new Party(12L, "SGP", "Staatkundig Gereformeerde Partij — behoudende christelijke partij met nadruk op religieuze waarden.", "https://logodix.com/logo/2132331.png"));
        parties.add(new Party(13L, "ChristenUnie", "ChristenUnie — christelijk-sociaal met aandacht voor gezin, ethiek en duurzaamheid.", "https://tse4.mm.bing.net/th/id/OIP.uscSAhjcdHlfnjkCAff4HgHaC-?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));
        parties.add(new Party(14L, "Volt", "Volt — pan-Europese progressieve partij, met visie voor Europese integratie en vernieuwing.", "https://th.bing.com/th/id/R.655284ef6d6972f957dbe88741f6624c?rik=MJfZy4vY6scg2Q&pid=ImgRaw&r=0"));
        parties.add(new Party(15L, "JA21", "JA21 — conservatief-liberale partij, met nadruk op identiteits- en veiligheidsbeleid.", "https://verkiezingen101.nl/wp-content/uploads/2021/02/1200px-JA21_logo.svg_-1024x1024.png"));
        parties.add(new Party(16L, "Vrede voor Dieren", "Vrede voor Dieren — dierenwelzijnsgerichte partij, inzet op restricties tegen dierenleed.", "https://samen1.nl/bestanden/uploads/vrede-voor-dieren.png"));
        parties.add(new Party(17L, "BVNL", "Belang Van Nederland — sterk in vraagstukken rond nationale identiteit, integratie, veiligheid.", "https://tse3.mm.bing.net/th/id/OIP.74bZqC-2w1arcYyICkHVuwHaCI?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));
        parties.add(new Party(18L, "BIJ1", "BIJ1 — partij tegen rascisme en discriminatie, voor sociale en economische rechtvaardigheid.", "https://www.rug.nl/research/dnpp/politieke-partijen/images/logo-bij1.jpg"));
        parties.add(new Party(19L, "LP", "Libertaire Partij — voor maximale individuele vrijheid en minimale staatsinmenging.", "https://www.rug.nl/research/dnpp/politieke-partijen/lp/logo-lp-2017.png"));
        parties.add(new Party(20L, "50PLUS", "50PLUS — partij die de belangen van ouderen vertegenwoordigt.", "https://th.bing.com/th/id/R.61397c44794ba037337bc35ed54bd06d?rik=ievGvFRHNwIkrQ&pid=ImgRaw&r=0"));
        parties.add(new Party(21L, "Piratenpartij", "Piratenpartij — inzet op digitale vrijheden, open data en privacy.", "https://piratenpartij.nl/wp-content/uploads/2013/08/pplogo-st-fixed.png"));
        parties.add(new Party(22L, "FNP", "Friese Nationale Partij — regionale partij met focus op Friese belangen.", "https://www.rug.nl/research/dnpp/politieke-partijen/fnp/logo-fnp.png"));
        parties.add(new Party(23L, "Vrij Verbond", "Vrij Verbond — partij die de vrije samenleving, autonomie en keuzevrijheid hoog houdt.", "https://tse3.mm.bing.net/th/id/OIP.qd1VWOLpFgRCqDyTPHH_lQAAAA?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));
        parties.add(new Party(24L, "DE LINIE", "DE LINIE — regionale partij, actief in specifieke kieskringen met lokaal beleid.", "https://deliniepartij.nl/wp-content/uploads/2025/08/LOGO.png"));
        parties.add(new Party(25L, "NL PLAN", "NL PLAN — planmatige partij, resoluties gebaseerd op lange termijn visie.", "https://irp.cdn-website.com/e843fd98/dms3rep/multi/nlplan_logo_2023-01.png"));
        parties.add(new Party(26L, "ELLECT", "ELLECT — jonge politieke beweging, gericht op vernieuwing en burgerparticipatie.", "https://www.ellect.eu/uploads/YgoQRJ4Q/87x0_87x0/logoklein_356__msi___png.webp"));
        parties.add(new Party(27L, "Partij voor de Rechtsstaat", "Partij voor de Rechtsstaat — nadruk op recht, rechtszekerheid en rechtsorde.", "https://tse1.mm.bing.net/th/id/OIP.4s8hG1Ol4vTPQ9amJsTwFQHaCA?cb=12&rs=1&pid=ImgDetMain&o=7&rm=3"));
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

