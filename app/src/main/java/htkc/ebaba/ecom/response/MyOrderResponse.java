package htkc.ebaba.ecom.response;

public class MyOrderResponse {
    private  String order_id,sum,status,date,success,product,o_id;

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    public String getO_id() {
        return o_id;
    }

    public void setOrder_id(String order_id) {
        order_id = order_id;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getSum() {
        return sum;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getSuccess() {
        return success;
    }

    public String getProduct() {
        return product;
    }
}
