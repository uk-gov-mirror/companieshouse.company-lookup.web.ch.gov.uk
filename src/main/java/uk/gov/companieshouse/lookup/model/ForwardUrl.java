package uk.gov.companieshouse.lookup.model;


import java.util.Objects;
import uk.gov.companieshouse.lookup.validation.RelativeUrl;

public class ForwardUrl {

    @RelativeUrl
    private String forward;

    public ForwardUrl(String forward) {
        this.forward = forward;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForwardUrl)) return false;
        ForwardUrl that = (ForwardUrl) o;
        return Objects.equals(getForward(), that.getForward());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getForward());
    }
}
