package htkc.ebaba.ecom.response;

public class DeliveryResponse {
    private String min,charges;

    public void setMin(String min) {
        this.min = min;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getCharges() {
        return charges;
    }

    public String getMin() {
        return min;
    }
}
