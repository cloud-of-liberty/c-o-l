package jp.col.Model;

public class ApplyTypeModel {
 
	//id
    private String id;
	//名称
    private String name;
    //sfid
    private String sfid;
	//テンプレート内容
    private String content;
    
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
	public String getSfid() {
		return sfid;
	}
	public void setSfid(String sfid) {
		this.sfid = sfid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}