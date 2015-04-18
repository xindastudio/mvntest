function createApplet(containerId, appletId, attrs, params) {
	if (!deployJava.versionCheck("1.6+")) {
		if (confirm("您的浏览器未安装Java插件，是否进入下载、安装向导？")) {
			window.open("http://www.java.com/zh_CN/download/index.jsp");
		}
		return;
	}
	var attributes = {
		id : appletId,
		name : appletId,
		codebase_lookup : "/",
		archive : "httpspeedtest.jar?time=" + new Date().getTime(),
		code : "huaxia.netspeed.ui.SpeedTestApplet",
		java_arguments : "-Djnlp.packEnabled=true",
		width : 288,
		height : 10,
		centerimage : true,
		boxborder : false,
		cache_option : "No"
	};
	var parameters = {
		hptThreadCount : "10",
		hptBufSize : "10240",
		hptTestTime : "15",
		hptDelayTime : "4",
		hptTimeout : "10",
		hptPeakPercentage : "0.7",
		hwtTimeout : "10",
		serverIps : "58.32.224.18",
		pingDestIP : "218.1.1.189",
		dnThreadCount : "10",
		upThreadCount : "16",
		pingThreadCount : "10",
		tcpBufSize : "10240",
		dnTestTime : "60",
		upTestTime : "30",
		tcpDelayTime : "0",
		tcpTimeout : "30",
		tcpPeakPercentage : "0.7",
		pingCount : "100"
	};
	if (attrs) {
		for (key in attrs) {
			attributes[key] = attrs[key];
		}
	}
	if (params) {
		for (key in params) {
			parameters[key] = params[key];
		}
	}
	var neededVer = "1.6";
	var obj_appletdiv = document.getElementById(containerId);
	deployJava.setInstallerType("kernel");
	deployJava.runApplet(attributes, parameters, neededVer, obj_appletdiv);
}

function getApplet(appletId) {
	return document.applets[appletId];
}

function createFlex(containerId, flexId, attrs, params) {
	if (!swfobject.hasFlashPlayerVersion("9.0.0")) {
		if (confirm("您的浏览器未安装FLASH插件或版本过低，是否进入下载、安装向导？")) {
			window.open("http://get.adobe.com/cn/flashplayer/");
		}
		return;
	}
	var attributes = {
		id : flexId,
		name : flexId
	};
	var parameters = {
		hptThreadCount : "10",
		hptBufSize : "10240",
		hptTestTime : "15",
		hptDelayTime : "4",
		hptTimeout : "10",
		hptPeakPercentage : "0.7",
		hwtTimeout : "10",
		serverIps : "58.32.224.18",
		pingDestIP : "218.1.1.189",
		dnThreadCount : "10",
		upThreadCount : "16",
		pingThreadCount : "10",
		tcpBufSize : "10240",
		dnTestTime : "60",
		upTestTime : "30",
		tcpDelayTime : "0",
		tcpTimeout : "30",
		tcpPeakPercentage : "0.7",
		pingCount : "100"
	};
	var flexparams = {
		wmode : "opaque",
		allowFullScreen : "false"
	};
	var archive = "SpeedTest.swf?time=" + new Date().getTime();
	var width = "288";
	var height = "10";
	if (attrs) {
		for (key in attrs) {
			attributes[key] = attrs[key];
		}
		if (attrs["archive"]) {
			archive = attrs["archive"];
		}
		if (attrs["width"]) {
			width = attrs["width"];
		}
		if (attrs["height"]) {
			height = attrs["height"];
		}
	}
	if (params) {
		for (key in params) {
			parameters[key] = params[key];
		}
	}
	swfobject.embedSWF(archive, containerId, width, height, "9.0.0",
			"expressInstall.swf", parameters, flexparams, attributes);
}

function getFlex(flexId) {
	return swfobject.getObjectById(flexId);
}

function createPlugin(containerId, pluginId, attrs, params, type) {
	if ("applet" == type) {
		createApplet(containerId, pluginId, attrs, params);
	} else {
		createFlex(containerId, pluginId, attrs, params);
	}
}

function getPlugin(pluginId, type) {
	if ("applet" == type) {
		return getApplet(pluginId);
	} else {
		return getFlex(pluginId);
	}
}
