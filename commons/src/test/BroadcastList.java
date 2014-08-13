package test;

import java.util.List;

public class BroadcastList {

	private String ChannelId;

	private String PlayDate;

	private String ListName;

	private String ListType;

	private String Version;

	private String TuwenVersion;

	private String CreateTime;

	private String Creator;

	private String AuditTime;

	private String ListId;

	private String programTotal;

	private List<ListItem> ListItems;

	public String getChannelId(){
		return ChannelId;
	}

	public void setChannelId(String ChannelId){
		this.ChannelId = ChannelId;
	}

	public String getPlayDate(){
		return PlayDate;
	}

	public void setPlayDate(String PlayDate){
		this.PlayDate = PlayDate;
	}

	public String getListName(){
		return ListName;
	}

	public void setListName(String ListName){
		this.ListName = ListName;
	}

	public String getListType(){
		return ListType;
	}

	public void setListType(String ListType){
		this.ListType = ListType;
	}

	public String getVersion(){
		return Version;
	}

	public void setVersion(String Version){
		this.Version = Version;
	}

	public String getTuwenVersion(){
		return TuwenVersion;
	}

	public void setTuwenVersion(String TuwenVersion){
		this.TuwenVersion = TuwenVersion;
	}

	public String getCreateTime(){
		return CreateTime;
	}

	public void setCreateTime(String CreateTime){
		this.CreateTime = CreateTime;
	}

	public String getCreator(){
		return Creator;
	}

	public void setCreator(String Creator){
		this.Creator = Creator;
	}

	public String getAuditTime(){
		return AuditTime;
	}

	public void setAuditTime(String AuditTime){
		this.AuditTime = AuditTime;
	}

	public String getListId(){
		return ListId;
	}

	public void setListId(String ListId){
		this.ListId = ListId;
	}

	public String getprogramTotal(){
		return programTotal;
	}

	public void setprogramTotal(String programTotal){
		this.programTotal = programTotal;
	}

	public List<ListItem> getListItems(){
		return ListItems;
	}

	public void setListItems(List<ListItem> ListItems){
		this.ListItems = ListItems;
	}


}