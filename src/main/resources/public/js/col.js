function saveDailyReport(){
	var wt1h=document.getElementById("workTime1Hours").value;
	var wt1m=document.getElementById("workTime1Minutes").value;
	var wt2h=document.getElementById("workTime2Hours").value;
	var wt2m=document.getElementById("workTime2Minutes").value;
	var wt3h=document.getElementById("workTime3Hours").value;
	var wt3m=document.getElementById("workTime3Minutes").value;
	var wt4h=document.getElementById("workTime4Hours").value;
	var wt4m=document.getElementById("workTime4Minutes").value;
	var wt5h=document.getElementById("workTime5Hours").value;
	var wt5m=document.getElementById("workTime5Minutes").value;
	var wt6h=document.getElementById("workTime6Hours").value;
	var wt6m=document.getElementById("workTime6Minutes").value;
	var wt7h=document.getElementById("workTime7Hours").value;
	var wt7m=document.getElementById("workTime7Minutes").value;
	var wt8h=document.getElementById("workTime8Hours").value;
	var wt8m=document.getElementById("workTime8Minutes").value;
	var wt9h=document.getElementById("workTime9Hours").value;
	var wt9m=document.getElementById("workTime9Minutes").value;
	var wt10h=document.getElementById("workTime10Hours").value;
	var wt10m=document.getElementById("workTime10Minutes").value;
	wt1h=(+wt1h)+(+wt1m/60);
	wt2h=(+wt2h)+(+wt2m/60);
	wt3h=(+wt3h)+(+wt3m/60);
	wt4h=(+wt4h)+(+wt4m/60);
	wt5h=(+wt5h)+(+wt5m/60);
	wt6h=(+wt6h)+(+wt6m/60);
	wt7h=(+wt7h)+(+wt7m/60);
	wt8h=(+wt8h)+(+wt8m/60);
	wt9h=(+wt9h)+(+wt9m/60);
	wt10h=(+wt10h)+(+wt10m/60);
	whs=(+wt1h)+(+wt2h)+(+wt3h)+(+wt4h)+(+wt5h)+(+wt6h)+(+wt7h)+(+wt8h)+(+wt9h)+(+wt10h)+"時間";
	var jt=document.getElementById("jobTime").innerText;
	var wk=document.getElementById("workKind").value;
	if(wk!="Absence"&&jt!=whs){
	   alert("仕事時間と勤務時間は不一致になります。");
	   return;
	}
	for(var i=1;i<=9;i++){
		for(var j=(+i)+1;j<=10;j++){
			if(document.getElementById("project"+j).value==null||document.getElementById("project"+j).value == undefined||document.getElementById("project"+j).value==''){
				}else if(document.getElementById("project"+i).value==document.getElementById("project"+j).value){
					alert("プロジェクト重複になりました。");
					return;
			}
		}	
	}
	var ct=document.getElementById("comment").value;
	if(wk=="Absence"&&ct.replace(/(^s*)|(s*$)/g, "").length ==0){
		alert("欠勤の場合、備考に欠勤の理由を入力してください。");
		return;
	}
    document.form1.action="/saveDailyReport";
    document.form1.submit();
}
function commitDailyReport(){
	if (confirm('提出します、よろしいでしょうか。')) {
		var wt1h=document.getElementById("workTime1Hours").value;
		var wt1m=document.getElementById("workTime1Minutes").value;
		var wt2h=document.getElementById("workTime2Hours").value;
		var wt2m=document.getElementById("workTime2Minutes").value;
		var wt3h=document.getElementById("workTime3Hours").value;
		var wt3m=document.getElementById("workTime3Minutes").value;
		var wt4h=document.getElementById("workTime4Hours").value;
		var wt4m=document.getElementById("workTime4Minutes").value;
		var wt5h=document.getElementById("workTime5Hours").value;
		var wt5m=document.getElementById("workTime5Minutes").value;
		var wt6h=document.getElementById("workTime6Hours").value;
		var wt6m=document.getElementById("workTime6Minutes").value;
		var wt7h=document.getElementById("workTime7Hours").value;
		var wt7m=document.getElementById("workTime7Minutes").value;
		var wt8h=document.getElementById("workTime8Hours").value;
		var wt8m=document.getElementById("workTime8Minutes").value;
		var wt9h=document.getElementById("workTime9Hours").value;
		var wt9m=document.getElementById("workTime9Minutes").value;
		var wt10h=document.getElementById("workTime10Hours").value;
		var wt10m=document.getElementById("workTime10Minutes").value;
		wt1h=(+wt1h)+(+wt1m/60);
		wt2h=(+wt2h)+(+wt2m/60);
		wt3h=(+wt3h)+(+wt3m/60);
		wt4h=(+wt4h)+(+wt4m/60);
		wt5h=(+wt5h)+(+wt5m/60);
		wt6h=(+wt6h)+(+wt6m/60);
		wt7h=(+wt7h)+(+wt7m/60);
		wt8h=(+wt8h)+(+wt8m/60);
		wt9h=(+wt9h)+(+wt9m/60);
		wt10h=(+wt10h)+(+wt10m/60);
		whs=(+wt1h)+(+wt2h)+(+wt3h)+(+wt4h)+(+wt5h)+(+wt6h)+(+wt7h)+(+wt8h)+(+wt9h)+(+wt10h)+"時間";
		var jt=document.getElementById("jobTime").innerText;
		var wk=document.getElementById("workKind").value;
		if(wk!="Absence"&&jt!=whs){
		alert("仕事時間と勤務時間は不一致になります。");
		return;
		}
		for(var i=1;i<=9;i++){
			for(var j=(+i)+1;j<=10;j++){
				if(document.getElementById("project"+j).value==null||document.getElementById("project"+j).value == undefined||document.getElementById("project"+j).value==''){
					}else if(document.getElementById("project"+i).value==document.getElementById("project"+j).value){
						alert("プロジェクト重複になりました。");
						return;
				}
			}	
		}
		var ct=document.getElementById("comment").value;
		if(wk=="Absence"&&ct.replace(/(^s*)|(s*$)/g, "").length ==0){
			alert("欠勤の場合、備考に欠勤の理由を入力してください。");
			return;
		}
	    document.form1.action="/commitDailyReport";
	    document.form1.submit();
	}
}

function encrypt(pass){
     var srcs = CryptoJS.enc.Utf8.parse(pass);
     var key = CryptoJS.enc.Utf8.parse("cloutofliberty_k");
     var iv = CryptoJS.enc.Utf8.parse("cloutofliberty_i");
     var encrypted = CryptoJS.AES.encrypt(srcs, key, 
    		 {
    	        iv: iv,
    	 		mode:CryptoJS.mode.CBC,
    	 		padding: CryptoJS.pad.Pkcs7
    	 	});
     return encrypted.toString();
}
function decrypt(word){  
     var key = CryptoJS.enc.Utf8.parse("cloutofliberty_k");   
     var iv = CryptoJS.enc.Utf8.parse("cloutofliberty_i");
     var decrypted = CryptoJS.AES.decrypt(word, key,
    		 {
    	        iv: iv,
    	 		mode:CryptoJS.mode.CBC,
    	 		padding: CryptoJS.pad.Pkcs7
    		 });
     return decrypted.toString(CryptoJS.enc.Utf8);  
}

function encryptPass(){
    document.formlogin.password.value = encrypt(document.formlogin.password.value);
    document.formlogin.submit();
}

function changeApplyType(){
    if (document.getElementById("applyType").value != "" ) {
        document.form1.action = "/NewApply";
        document.form1.submit();
    }
}

function opencloseuserinfo(sectionid) {
	var section = document.getElementById(sectionid);
	if (section.style.display != "none") {
		section.style.display = "none";
	} else {
		section.style.display = "block";
	}	
}
function click1(){
	var sh=document.getElementById("sh").value;
	var sm=document.getElementById("sm").value;
	var eh=document.getElementById("eh").value;
	var em=document.getElementById("em").value;
	var bh=document.getElementById("bh").value;
	var bm=document.getElementById("bm").value;
	sh=(+sh)+(+sm/60);
	eh=(+eh)+(+em/60);
	bh=(+bh)+(+bm/60);
	jt=eh-sh-bh;
	document.getElementById("jobTime").innerHTML=jt+"時間";		
}
function click2(){
	document.form1.action="/cancelReport";
	document.form1.submit();
}
history.pushState(null, null, document.URL);
window.addEventListener('popstate', function () {
    history.pushState(null, null, document.URL);
});


