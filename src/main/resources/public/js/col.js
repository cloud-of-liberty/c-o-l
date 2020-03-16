function saveDailyReport(){
    document.form1.action="/saveDailyReport";
    document.form1.submit();
}
function commitDailyReport(){
	if (confirm('提出します、よろしいでしょうか。')) {
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

history.pushState(null, null, document.URL);
window.addEventListener('popstate', function () {
    history.pushState(null, null, document.URL);
});


