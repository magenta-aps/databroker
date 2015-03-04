package dk.magenta.databroker.cvr.model.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class ValidFromField {
    @Column
    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    @Column
    private String text;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
