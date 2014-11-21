package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.model.adgangspunkt.AdgangspunktRepository;
import dk.magenta.databroker.cprvejregister.model.adresse.AdresseRepository;
import dk.magenta.databroker.cprvejregister.model.doerpunkt.DoerpunktRepository;
import dk.magenta.databroker.cprvejregister.model.husnummer.HusnummerRepository;
import dk.magenta.databroker.cprvejregister.model.isopunkt.IsoPunktRepository;
import dk.magenta.databroker.cprvejregister.model.kommune.KommuneRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.navngivenvej.NavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.postnummer.PostnummerRepository;
import dk.magenta.databroker.cprvejregister.model.reserverethusnummerinterval.ReserveretHusnrIntervalRepository;
import dk.magenta.databroker.cprvejregister.model.reserveretvejnavn.ReserveretVejnavnRepository;
import dk.magenta.databroker.cprvejregister.model.vejnavneforslag.VejnavneforslagRepository;
import dk.magenta.databroker.cprvejregister.model.vejnavneomraade.VejnavneomraadeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 21-11-14.
 */
public class RepositoryCollection {
    /*public static final String ADGANGSPUNKT = "adgangspunkt";
    public static final String ADRESSE = "adresse";
    public static final String DOERPUNKT = "doerpunkt";
    public static final String HUSNUMMER = "husnummer";
    public static final String ISOPUNKT = "isopunkt";
    public static final String KOMMUNE = "kommune";
    public static final String VEJDEL = "vejdel";
    public static final String VEJ = "vej";
    public static final String POSTNUMMER = "postnummer";
    public static final String RESERVERETINTERVAL = "reserveretinterval";
    public static final String RESERVERETVEJNAVN = "reserveretvejnavn";
    public static final String VEJNAVNEFORSLAG = "vejnavneforslag";
    public static final String VEJNAVNEOMRAADE = "vejnanveomraade";*/

    public AdgangspunktRepository adgangspunktRepository;
    public AdresseRepository adresseRepository;
    public DoerpunktRepository doerpunktRepository;
    public HusnummerRepository husnummerRepository;
    public IsoPunktRepository isoPunktRepository;
    public KommuneRepository kommuneRepository;
    public KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;
    public NavngivenVejRepository navngivenVejRepository;
    public PostnummerRepository postnummerRepository;
    public ReserveretHusnrIntervalRepository reserveretHusnrIntervalRepository;
    public ReserveretVejnavnRepository reserveretVejnavnRepository;
    public VejnavneforslagRepository vejnavneforslagRepository;
    public VejnavneomraadeRepository vejnavneomraadeRepository;

    public RegistreringRepository registreringRepository;

}
