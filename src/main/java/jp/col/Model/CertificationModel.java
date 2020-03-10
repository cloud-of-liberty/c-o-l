package jp.col.Model;

public class CertificationModel {
 
    private String id;
    private String name;
    private String certificationField;
    private String certificationSyutokubi;
	private String bikou;
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
	public String getCertificationField() {
        return certificationField;
    }
 
	public void setCertificationField(String certificationField) {
        this.certificationField = certificationField;
    }
 
    public String getCertificationSyutokubi() {
        return certificationSyutokubi;
    }
 
    public void setCertificationSyutokubi(String certificationSyutokubi) {
        this.certificationSyutokubi = certificationSyutokubi;
    }
 
    public String getBikou() {
        return bikou;
    }
 
    public void setBikou(String bikou) {
        this.bikou = bikou;
    }
}