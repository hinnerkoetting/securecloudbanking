// ==UserScript==
// @name Emulate Greasemonkey functions
// @author TarquinWJ 
// @namespace http://www.howtocreate.co.uk/ 
// @version 1.1.1
// @description  Attempts to replicate the Greasemonkey functions in
//			Opera.
// @ujs:category browser: enhancements
// @ujs:published 2005-05-30 23:16
// @ujs:modified 2005-10-27 12:57
// @ujs:documentation http://userjs.org/scripts/browser/enhancements/aa-gm-functions 
// @ujs:download http://userjs.org/scripts/download/browser/enhancements/aa-gm-functions.js
// ==/UserScript==


/* 
 * Please see
 * http://www.howtocreate.co.uk/operaStuff/userJavaScript.html#terms
 * for License and Terms of Use
 */

function GM_setValue( cookieName, cookieValue, lifeTime ) {
	if( !cookieName ) { return; }
	if( lifeTime == "delete" ) { lifeTime = -10; } else { lifeTime = 31536000; }
	document.cookie = escape( cookieName ) + "=" + escape( cookieValue ) +
		";expires=" + ( new Date( ( new Date() ).getTime() + ( 1000 * lifeTime ) ) ).toGMTString() + ";path=/";
}

function GM_getValue( cookieName, oDefault ) {
	var cookieJar = document.cookie.split( "; " );
	for( var x = 0; x < cookieJar.length; x++ ) {
		var oneCookie = cookieJar[x].split( "=" );
		if( oneCookie[0] == escape( cookieName ) ) {
			try {
				eval('var footm = '+unescape( oneCookie[1] ));
			} catch(e) { return oDefault; }
			return footm;
		}
	}
	return oDefault;
}

function GM_deleteValue( oKey ) {
	//yes, they didn't seem to provide a way to delete variables in Greasemonkey, and the user must use about:config to
	//delete them - so the stored variables will pile up forever ...
	GM_setValue( oKey, '', 'delete' );
}

var GM_falsifiedMenuCom = [], hasPageGMloaded = false;
window.addEventListener('load',function () {hasPageGMloaded=true;doGMMeenoo();},false)
function GM_registerMenuCommand( oText, oFunc ) {
	GM_falsifiedMenuCom[GM_falsifiedMenuCom.length] = [oText,oFunc];
	if( hasPageGMloaded ) { doGMMeenoo(); } //if the page has already loaded, do it now
}

function doGMMeenoo() {
	if( !GM_falsifiedMenuCom.length ) { return; }
	//create a menu of commands in the top corner
	var foo = document.getElementById('GM_Falsify_me'), bar;
	if( foo ) { document.body.removeChild(foo); }
	foo = document.createElement('GMmenoo');
	foo.id = 'GM_Falsify_me';
	document.body.appendChild(foo);
	with( foo.style ) {
		border = '1px solid #000';
		backgroundColor = '#bbf';
		color = '#000';
		position = 'fixed';
		zIndex = '100000';
		top = '0px';
		right = '0px';
		padding = '2px';
		overflow = 'hidden';
		height = '1.3em';
	}
	foo.appendChild(bar = document.createElement('b'))
	bar.style.cursor = 'move';
	bar.onclick = function () {
		this.parentNode.style.left = this.parentNode.style.left ? '' : '0px';
		this.parentNode.style.right = this.parentNode.style.right ? '' : '0px';
	};
	bar.appendChild(document.createTextNode('User Script Commands'));
	foo.appendChild(bar = document.createElement('ul'));
	bar.style.margin = '0px';
	bar.style.padding = '0px';
	bar.style.listStylePosition = 'inside';
	for( var i = 0; GM_falsifiedMenuCom[i]; i++ ) {
		var baz = document.createElement('li'), bing;
		baz.appendChild(bing = document.createElement('a'));
		bing.setAttribute('href','#');
		bing.onclick = new Function('GM_falsifiedMenuCom['+i+'][1](arguments[0]);return false;');
		bing.onfocus = function () { this.parentNode.style.height = ''; };
		bing.onblur = function () { this.parentNode.style.height = '1.3em'; };
		bing.appendChild(document.createTextNode(GM_falsifiedMenuCom[i][0]));
		bar.appendChild(baz);
	}
	foo.onmouseover = function () { this.style.height = ''; };
	foo.onmouseout = function () { this.style.height = '1.3em'; };
}

//yes, I know the limitations, but it's better than an outright error
GM_xmlhttpRequest = XMLHttpRequest;

GM_log = opera.postError;

window._content = window;

function getRecoverableString(oVar,notFirst) {
	var oType = typeof(oVar);
	if( ( oType == 'null' ) || ( oType == 'object' && !oVar ) ) {
		//most browsers say that the typeof for null is 'object', but unlike a real
		//object, it will not have any overall value
		return 'null';
	}
	if( oType == 'undefined' ) { return 'window.uDfXZ0_d'; }
	if( oType == 'object' ) {
		//Safari throws errors when comparing non-objects with window/document/etc
		if( oVar == window ) { return 'window'; }
		if( oVar == document ) { return 'document'; }
		if( oVar == document.body ) { return 'document.body'; }
		if( oVar == document.documentElement ) { return 'document.documentElement'; }
	}
	if( oVar.nodeType && ( oVar.childNodes || oVar.ownerElement ) ) { return '{error:\'DOM node\'}'; }
	if( !notFirst ) {
		Object.prototype.toRecoverableString = function (oBn) {
			if( this.tempLockIgnoreMe ) { return '{\'LoopBack\'}'; }
			this.tempLockIgnoreMe = true;
			var retVal = '{', sepChar = '', j;
			for( var i in this ) {
				if( i == 'toRecoverableString' || i == 'tempLockIgnoreMe' || i == 'prototype' || i == 'constructor' ) { continue; }
				if( oBn && ( i == 'index' || i == 'input' || i == 'length' || i == 'toRecoverableObString' ) ) { continue; }
				j = this[i];
				if( !i.match(basicObPropNameValStr) ) {
					//for some reason, you cannot use unescape when defining peoperty names inline
					for( var x = 0; x < cleanStrFromAr.length; x++ ) {
						i = i.replace(cleanStrFromAr[x],cleanStrToAr[x]);
					}
					i = '\''+i+'\'';
				} else if( window.ActiveXObject && navigator.userAgent.indexOf('Mac') + 1 && !navigator.__ice_version && window.ScriptEngine && ScriptEngine() == 'JScript' && i.match(/^\d+$/) ) {
					//IE mac does not allow numerical property names to be used unless they are quoted
					i = '\''+i+'\'';
				}
				retVal += sepChar+i+':'+getRecoverableString(j,true);
				sepChar = ',';
			}
			retVal += '}';
			this.tempLockIgnoreMe = false;
			return retVal;
		};
		Array.prototype.toRecoverableObString = Object.prototype.toRecoverableString;
		Array.prototype.toRecoverableString = function () {
			if( this.tempLock ) { return '[\'LoopBack\']'; }
			if( !this.length ) {
				var oCountProp = 0;
				for( var i in this ) { if( i != 'toRecoverableString' && i != 'toRecoverableObString' && i != 'tempLockIgnoreMe' && i != 'prototype' && i != 'constructor' && i != 'index' && i != 'input' && i != 'length' ) { oCountProp++; } }
				if( oCountProp ) { return this.toRecoverableObString(true); }
			}
			this.tempLock = true;
			var retVal = '[';
			for( var i = 0; i < this.length; i++ ) {
				retVal += (i?',':'')+getRecoverableString(this[i],true);
			}
			retVal += ']';
			delete this.tempLock;
			return retVal;
		};
		Boolean.prototype.toRecoverableString = function () {
			return ''+this+'';
		};
		Date.prototype.toRecoverableString = function () {
			return 'new Date('+this.getTime()+')';
		};
		Function.prototype.toRecoverableString = function () {
			return this.toString().replace(/^\s+|\s+$/g,'').replace(/^function\s*\w*\([^\)]*\)\s*\{\s*\[native\s+code\]\s*\}$/i,'function () {[\'native code\'];}');
		};
		Number.prototype.toRecoverableString = function () {
			if( isNaN(this) ) { return 'Number.NaN'; }
			if( this == Number.POSITIVE_INFINITY ) { return 'Number.POSITIVE_INFINITY'; }
			if( this == Number.NEGATIVE_INFINITY ) { return 'Number.NEGATIVE_INFINITY'; }
			return ''+this+'';
		};
		RegExp.prototype.toRecoverableString = function () {
			return '\/'+this.source+'\/'+(this.global?'g':'')+(this.ignoreCase?'i':'');
		};
		String.prototype.toRecoverableString = function () {
			var oTmp = escape(this);
			if( oTmp == this ) { return '\''+this+'\''; }
			return 'unescape(\''+oTmp+'\')';
		};
	}
	if( !oVar.toRecoverableString ) { return '{error:\'internal object\'}'; }
	var oTmp = oVar.toRecoverableString();
	if( !notFirst ) {
		//prevent it from changing for...in loops that the page may be using
		delete Object.prototype.toRecoverableString;
		delete Array.prototype.toRecoverableObString;
		delete Array.prototype.toRecoverableString;
		delete Boolean.prototype.toRecoverableString;
		delete Date.prototype.toRecoverableString;
		delete Function.prototype.toRecoverableString;
		delete Number.prototype.toRecoverableString;
		delete RegExp.prototype.toRecoverableString;
		delete String.prototype.toRecoverableString;
	}
	return oTmp;
}
var basicObPropNameValStr = /^\w+$/, cleanStrFromAr = new Array(/\\/g,/'/g,/"/g,/\r/g,/\n/g,/\f/g,/\t/g,new RegExp('-'+'->','g'),new RegExp('<!-'+'-','g'),/\//g), cleanStrToAr = new Array('\\\\','\\\'','\\\"','\\r','\\n','\\f','\\t','-\'+\'->','<!-\'+\'-','\\\/');