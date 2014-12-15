package dk.magenta.databroker.cprvejregister.dataproviders;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import dk.magenta.databroker.core.DataProvider;
import dk.magenta.databroker.core.model.DataProviderEntity;
import dk.magenta.databroker.core.model.DataProviderStorageEntity;
import dk.magenta.databroker.core.model.DataProviderStorageRepository;
import dk.magenta.databroker.core.model.oio.RegistreringEntity;
import dk.magenta.databroker.core.model.oio.RegistreringRepository;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejEntity;
import dk.magenta.databroker.cprvejregister.model.kommunedelafnavngivenvej.KommunedelAfNavngivenVejRepository;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetEntity;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetRepository;
import dk.magenta.databroker.cprvejregister.model.lokalitet.LokalitetVersionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lars on 12-12-14.
 */
@Component
public class GrLokalitetsRegister extends DataProvider {

    @Autowired
    private DataProviderStorageRepository dataProviderStorageRepository;

    private DataProviderStorageEntity storageEntity;

    @Autowired
    private LokalitetRepository lokalitetRepository;

    @Autowired
    private KommunedelAfNavngivenVejRepository kommunedelAfNavngivenVejRepository;

    @Autowired
    private RegistreringRepository registreringRepository;


    public GrLokalitetsRegister(DataProviderEntity dbObject) {
        super(dbObject);
    }

    public GrLokalitetsRegister() {
    }

    @PostConstruct
    public void PostConstructGrLokalitetsRegister() {
        if (this.dataProviderStorageRepository != null) {
            DataProviderStorageEntity storageEntity = this.dataProviderStorageRepository.getByOwningClass(this.getClass().getName());
            if (storageEntity == null) {
                storageEntity = new DataProviderStorageEntity();
                storageEntity.setOwningClass(this.getClass().getName());
                this.dataProviderStorageRepository.save(storageEntity);
            }
            this.storageEntity = storageEntity;
        }
        DataProviderEntity vejProvider = new DataProviderEntity();
        vejProvider.setUuid(UUID.randomUUID().toString());

        this.setDataProviderEntity(vejProvider);
    }


    protected String getEncoding() {
        return "UTF-8";
    }


    /*
    * Registration
    * */

    private RegistreringEntity createRegistrering;
    private RegistreringEntity updateRegistrering;

    private void createRegistreringEntities() {
        this.createRegistrering = registreringRepository.createNew(this);
        this.updateRegistrering = registreringRepository.createUpdate(this);
    }

    @Transactional
    public void pull() {
        InputStream input = this.readFile(new File("src/test/resources/grønlandLokaliteter.csv"));
        try {
            String encoding = this.getEncoding();
            if (encoding != null) {
                System.out.println("Using explicit encoding " + encoding);
            } else {
                // Try to guess the encoding based on the stream contents
                CharsetDetector detector = new CharsetDetector();
                detector.setText(input);
                CharsetMatch match = detector.detect();
                if (match != null) {
                    encoding = match.getName();
                    System.out.println("Interpreting data as " + encoding);
                } else {
                    encoding = "UTF-8";
                    System.out.println("Falling back to default encoding " + encoding);
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, encoding.toUpperCase()));

            System.out.println("Reading data");
            Date startTime = new Date();
            int i = 0, j = 0;
            createRegistreringEntities();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line != null) {
                    line = line.trim();
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        try {
                            int kommuneKode = Integer.parseInt(parts[0], 10);
                            int vejKode = Integer.parseInt(parts[1], 10);
                            String lokalitetsNavn = parts[2];
                            int lokalitetsKode = Integer.parseInt(parts[3], 10);

                            // Several input entries will share the same LokalitetEntity
                            LokalitetEntity lokalitetEntity = lokalitetRepository.findByLokalitetsKode(lokalitetsKode);
                            LokalitetVersionEntity lokalitetVersionEntity = null;
                            if (lokalitetEntity == null) {
                                lokalitetEntity = LokalitetEntity.create();
                                lokalitetEntity.setLokalitetsKode(lokalitetsKode);
                                lokalitetVersionEntity = lokalitetEntity.addVersion(createRegistrering);
                            } else if (!lokalitetsNavn.equals(lokalitetEntity.getLatestVersion().getLokalitetsNavn())) {
                                lokalitetVersionEntity = lokalitetEntity.addVersion(updateRegistrering);
                            }
                            // If there's anything to save, do it
                            if (lokalitetVersionEntity != null) {
                                lokalitetVersionEntity.setLokalitetsNavn(lokalitetsNavn);
                                this.lokalitetRepository.save(lokalitetEntity);
                            }


                            // Refer to the new/updated entity
                            KommunedelAfNavngivenVejEntity kommunedelAfNavngivenVejEntity = kommunedelAfNavngivenVejRepository.getByKommunekodeAndVejkode(kommuneKode, vejKode);
                            if (kommunedelAfNavngivenVejEntity != null && lokalitetEntity != kommunedelAfNavngivenVejEntity.getLokalitet()) {
                                kommunedelAfNavngivenVejEntity.setLokalitet(lokalitetEntity);
                                kommunedelAfNavngivenVejRepository.save(kommunedelAfNavngivenVejEntity);
                            }

                            // Complain if we can't find any
                            if (kommunedelAfNavngivenVejEntity == null) {
                                System.out.println("No kommune for "+lokalitetsNavn+" (KommuneKode: "+kommuneKode+", VejKode: "+vejKode+" not found)");
                            }

                        } catch (NumberFormatException e) {

                        }
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int getInt(String value) {
        try {
            return Integer.parseInt(value, 10);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
