	'use strict';
	//融合公用对象
	var platform_cordova = platform_cordova || {};
	
	document.addEventListener("deviceready", backbutton, false);

	function backbutton() {
		var time = new Date(); // 2s内再次点击就退出
	
		// 监听返回按钮
		document.addEventListener('backbutton', function(evt) {
			var pagename = platform_cordova.getPageName();
			// var urlList = location.href
			if(pagename == 'index' || pagename == 'index.html') { // 处于app首页,满足退出app操作
				if(new Date() - time < 2000) { // 小于2s,退出程序
					AppUtilsModule.exitApp({});
				} else { // 大于2s，重置时间戳，
					time = new Date();
				}
			}
			
		});
	}
	
	/**
	 * 系统平台， 0：全部， 1：理赔， 2：承保
	 */
	platform_cordova.sysPlatform = 0;
	
	//获取当前页面运行环境
    platform_cordova.environment = function() {
        var name = window.location.hostname;
        
        //太保环境
        if(name.indexOf('cpic') > 0){
        	name = name.split('.')[0];
        
	        if(name.indexOf('ft') > 0){
	        	return 'ft';
	        }else if(name.indexOf('sit') > 0){
	        	return 'sit';
	        }else{
	        	return 'online';
	        }
        }else{
        	return 'sit';
        }
        
    };
    
    /**
     * 二秒后消失的弹出框
	 * @param message:消息内容，只传字符串
	*/
	platform_cordova.toast=function(message){
		//创建prompt框样式
		var type="width:60%;padding:5px;line-height:20px; border-radius:15px; color:#fff; background-color:#6c6c6c; text-align:center;font-size:.8em; top:65%;opacity:0;position:fixed;left:20%;z-index:1003;";
		//创建prompt框
		var h=$("<div style='"+type+"' id='e_ui_prompt'>"+message+"</div>");
		$('body').append(h);

		//改变透明度
		var i=0;
		var t=setInterval(function(){
			i=i+0.1;
			if(i>=1){
				clearInterval(t);
				i=0;
			}else{
				$(h).css('opacity',i);
			}
		},80);

		//2秒后消失
		setTimeout(function(){
			var j=$(h).css('opacity');
			var ti=setInterval(function(){
				j=j-0.1;
				if(j<=0){
					clearInterval(ti);
					$(h).remove();
				}else{
					$(h).css('opacity',j);
				}
			},80);
		},5000);
	};
	
	//当前是否在壳子中运行
    platform_cordova.isRunByApp = function() {
        return platform_cordova.isAndroidApp() || platform_cordova.isIphoneApp();
    };
    
	/**
	 * 当前是否在iPhone壳子中运行
	 */
	platform_cordova.isAndroidApp = function() {
		var result = false;
		if(platform_cordova.getPlatform() == "android") {
			result = true;
		}
		return result;
	}

	/**
	 * 当前是否在iPhone壳子中运行
	 */
	platform_cordova.isIphoneApp = function() {
		var result = false;
		if(platform_cordova.getPlatform() == "ios" && (!/Safari/.test(navigator.userAgent))) {
			result = true;
		}
		return result;
	}

	platform_cordova.getPlatform = function() {
		var platforms = {
			amazon_fireos: /cordova-amazon-fireos/,
			android: /Android/,
			ios: /(iPad)|(iPhone)|(iPod)/,
			blackberry10: /(BB10)/,
			blackberry: /(PlayBook)|(BlackBerry)/,
			windows8: /MSAppHost/,
			windowsphone: /Windows Phone/
		};
		for(var key in platforms) {
			if(platforms[key].exec(navigator.userAgent)) {
				return key;
			}
		}
		return "";
	}

	//获取设置值
	platform_cordova.getValue = function(value, DEFAULT) {
		return typeof value == 'undefined' ? DEFAULT : value;
	}
	
	//获取当前设备的逻辑宽度和高度
    platform_cordova.viewport = function(){
    	return {
    		width: $(window).width(),
        	height: $(window).height()
    	};
    };

    //获取当前页面的名称
    platform_cordova.getPageName = function() {
        var href = window.location.href;
        var index = href.indexOf('?');
        if (index > 0) {
            href = href.substr(0, index);
        }
        index = href.lastIndexOf('\/');
        if (index > 0) {
            href = href.substr(index + 1);
        }
        return href;
    };
    
    /**
     *得到地址栏参数
     *@param names 参数名称
     *@param cn 是否中文
     *@returns string
     **/
    platform_cordova.getQueryString = function(names, cn) {
    	var urls = window.location.href;
        if (urls) {
            if (urls.indexOf('?') > -1) {
                urls = urls.substring(urls.indexOf('?') + 1);
            } else {
                return '';
            }
        } else {
            urls = window.location.search.substr(1);
        }

        var reg = new RegExp("(^|&)" + names + "=([^&]*)(&|$)", "i");
        var r = urls.match(reg);
        if (r && r[2]) {
            var ms = r[2].match(/(\<)|(\>)|(%3C)|(%3E)/g);
            if (ms && ms.length >= 4) {
                //如果检测到有2对及以上开始和结束尖括号
                r[2] = r[2].replace(/(\<)|(%3C)/g, '');
            }
            if (cn) {
                return r[2];
            }
            return unescape(r[2]);
        }
        return '';
    };
    
	//获取文件夹基础路径，便于动态加载资源
    platform_cordova.basePath = function() {
        var srcUrl = "";
        $("script").each(function() {
            if (/platform_cordova\.js/i.test($(this).attr("src"))) {
                srcUrl = $(this).attr("src").match(/(.*?)(js\/platform_cordova.js)/i)[1];
                return false;
            }
        });
        return srcUrl;
    };
    
    //创建时间，日期补0函数
    function p(s) {
        return s < 10 ? '0' + s: s;
    }
    
	//获取当前日期
    platform_cordova.currentDate = function() {
        var _d = new Date();
        var _s = _d.getFullYear() + '-' + p(_d.getMonth() + 1) + '-' + p(_d.getDate());
        
        return _s;
    };

	//获取当前时间
    platform_cordova.currentTime = function() {
        var _d = new Date();
        var _s = _d.getFullYear() + '-' + p(_d.getMonth() + 1) + '-' + p(_d.getDate());
        
        _s += ' ' + p(_d.getHours()) + ':' + p(_d.getMinutes()) + ':' + p(_d.getSeconds());
        
        return _s;
    };
    
	//设置本地缓存
    platform_cordova.setLocalStorage = function(key, value, isJson) {
        if (window.localStorage) {
            if (isJson) {
                value = JSON.stringify(value);
            }
            window.localStorage[key] = value;
        } else {
            e.log("当前浏览器不支持localStorage");
        }
    };

    //获取本地缓存
    platform_cordova.getLocalStorage = function(key, isJson) {
        if (window.localStorage) {
            var value = window.localStorage[key] || "";
            if (isJson && value) {
                value = JSON.parse(value);
            }
            return value;
        } else {
            e.log("当前浏览器不支持localStorage");
        }
    };
    
    //清除本地缓存removelocalStorage
    platform_cordova.removelocalStorage = function(key) {
        if (window.localStorage) {
            window.localStorage.removeItem(key);
        }
    };

    //设置运行时缓存setSessionStorage
    platform_cordova.setSessionStorage = function(key, content) {
        if (window.sessionStorage) {
            window.sessionStorage[key] = content;
        } else {
            e.log('您的浏览器不支持setSessionStorage方法');
        }
    };

    //获取运行时缓存getSessionStorage
    platform_cordova.getSessionStorage = function(key) {
        if (window.sessionStorage) {
            return window.sessionStorage[key] || '';
        } else {
            e.log('您的浏览器不支持getSessionStorage方法');
        }
    };

    //清除运行时缓存removeSessionStorage
    platform_cordova.removeSessionStorage = function(key) {
        if (window.sessionStorage) {
            window.sessionStorage.removeItem(key);
        }
    };

    //获取某个cookie
    platform_cordova.getCookie = function(cookie_name, decode) {
        //修改ACCESS_TOKEN从localStorage中获取
        if ('ACCESS_TOKEN' == cookie_name) {
            return e.getLocalStorage('ACCESS_TOKEN');
        }
        decode = decode || true;
        var allcookies = document.cookie;
        var cookie_pos = allcookies.indexOf(cookie_name);
        var value = '';
        if (cookie_pos > -1) {
            cookie_pos += cookie_name.length + 1;
            var cookie_end = allcookies.indexOf(";", cookie_pos);
            if (cookie_end == -1) {
                cookie_end = allcookies.length;
            }
            value = allcookies.substring(cookie_pos, cookie_end);
            if (decode) {
                try {
                    value = decodeURIComponent(value);
                } catch (e) {
                    console.log(e);
                    return "";
                }
            }
        }
        return value;
    };

    //设置某个cookie
    platform_cordova.setCookie = function(cookie_name, cookie_value, domain, isencode, expTime) {
        var exp = new Date();
        var expires = "";
        if (expTime) {
            exp.setTime(exp.getTime() + expTime);
            expires = ";expires=" + exp.toGMTString();
        }
        if (!domain) {
            domain = document.domain;
            if (/^[a-z]/i.test(document.domain)) {
                domain = document.domain.substring(document.domain.indexOf("."));
            }
        }

        isencode = typeof isencode == 'undefined' ? true : isencode;
        if (isencode) {
            cookie_value = encodeURIComponent(cookie_value);
        }
        //修改ACCESS_TOKEN 存储为localStorage形式，避免应用退出用户丢失，重新登录问题。
        if ('ACCESS_TOKEN' == cookie_name) {
            e.setLocalStorage('ACCESS_TOKEN', cookie_value);
            document.cookie = cookie_name + "=" + cookie_value + "; path=/; domain=" + domain + ";" + expires;
        } else {
            document.cookie = cookie_name + "=" + cookie_value + "; path=/; domain=" + domain + ";" + expires;
        }
    };
    
	//判定是否为空
    platform_cordova.isNull = function(value) {
        return typeof value == 'undefined' || value == "" || value == null || value == undefined|| value == "null";
    };
    
    //判定是否为手机号码
    platform_cordova.isMobile = function(value) {
        return (/^(1)\d{10}$/.test($.trim(value)));
    };
    
    //是否中文
    platform_cordova.isChinese = function(value) {
        return (/[^\u4e00-\u9fa5]/.test($.trim(value)));
    };
    
    //正整数检测
    platform_cordova.positiveNumber=function(value) {
        return ( /^[1-9]\d*$/.test($.trim(value)));
    };

    //复杂度检测  密码8-16位字母与数字的组合
    platform_cordova.isPassword=function(value){
        var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;//  复杂度检测  密码8-16位字母与数字的组合
        return reg.test(value);
    };
    
	//校验Url是否正确
	platform_cordova.validateUrl = function(urlStr)
	{
		var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
		var objExp=new RegExp(Expression);
		
		if(objExp.test(urlStr))
		{
			return true;
		}else
		{
			return false;
		}
	};
	
	//判断微信
	platform_cordova.isWeixin = function(){ 
		//判断是否是微信
		var ua = navigator.userAgent.toLowerCase();
		if(userAgent.indexOf('micromessenger') >= 0){
			return true;
		} else {
			return false;
		}
	}
	
	//判断支付宝
	platform_cordova.isAliApp = function(){ 
		//判断是否是支付宝
		var userAgent = navigator.userAgent.toLowerCase();
		if(userAgent.indexOf('aliapp') >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//判断QQ
	platform_cordova.isQQ = function(){ 
		//判断是否是QQ
		var userAgent = navigator.userAgent.toLowerCase();
		if(userAgent.indexOf('qq') >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//判断微博
	platform_cordova.isWeibo = function(){ 
		//判断是否是微博
		var userAgent = navigator.userAgent.toLowerCase();
		if(userAgent.indexOf('weibo') >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//获取当前域名
	platform_cordova.getDomain = function () {
	    if (/^\d/.test(document.domain)) 
	    {
	        return document.domain;
	    } else if (/^[a-z]/i.test(document.domain)) 
	    {
	        return document.domain.substring(document.domain.indexOf("."));
	    }
	    return document.domain;
	}
	
	//文件是否存在
    platform_cordova.isInclude = function(path) {
        var index = path.lastIndexOf('\/');
        var name = index > 0 ? path.substr(index + 1) : path;
        var css = /css$/i.test(name);
        var tag = 'link';
        var pt = 'href';
        var type = 'css';
        if (!css) {
            tag = 'script';
            pt = 'src';
            type = 'js';
        }

        var elements = document.getElementsByTagName(tag);
        for (var i = 0; i < elements.length; i++) {
            if (elements[i][pt].indexOf(name) > -1) {
                return [true, type];
            }
        }
        return [false, type];
    };

    //动态加载文件，只能加载CSS和js文件
    platform_cordova.loadFiles = function(pathArray, callback) {
        if (!$.isFunction(callback)) return;
        if (!pathArray || pathArray.length === 0) {
            callback({
                status: false,
                error: '请传入有效的路径'
            });
        }

        if ($.isArray(pathArray)) {
            var _index = 0;
            var _cycle = function(p, cb) {
                _loadSingleFile(p, function(pt) {
                    if (pt.status) {
                        _index++;
                        if (_index >= pathArray.length) {
                            cb({
                                status: true
                            });
                        } else {
                            window.setTimeout(function() {
                                _cycle(pathArray[_index], cb);
                            }, 100);
                        }
                    } else {
                        cb(pt);
                    }
                });
            };
            _cycle(pathArray[_index], callback);
        } else {
            _loadSingleFile(pathArray, callback);
        }

        function _loadSingleFile(path, callback) {
            ecpic.log('我要去加载' + path + '文件');
            var clude = ecpic.isInclude(path);
            if (clude[0]) {
                callback({
                    status: true
                });
                return;
            }

            var js = clude[1] === 'js';
            var el = document.getElementsByTagName('body')[0];
            var node = document.createElement('script');
            path += (path.indexOf('?') > -1 ? '&' : '?') + 'v=' + new Date().getTime();

            if (js) {
                node.type = 'text/javascript';
                node.src = path;
            } else {
                el = document.getElementsByTagName('head')[0];
                node = document.createElement('link');
                node.rel = 'stylesheet';
                node.type = 'text/css';
                node.href = path;
            }
            //所有浏览器加载css都会走onload方法
            node.onload = function() {
                if (js) {
                    callback({
                        status: true
                    });
                } else {
                    var _n = 0;
                    var _poll = function(node, callback) {
                        var isLoaded = false;
                        if (/webkit/i.test(navigator.userAgent)) {
                            //webkit
                            if (node.sheet) {
                                isLoaded = true;
                            }
                        } else if (node.sheet) {
                            // for Firefox
                            try {
                                if (node.sheet.cssRules) {
                                    isLoaded = true;
                                }
                            } catch (ex) {
                                // NS_ERROR_DOM_SECURITY_ERR
                                if (ex.code === 1000) {
                                    isLoaded = true;
                                }
                            }
                        }
                        if (isLoaded) {
                            callback({
                                status: true
                            });
                        } else {
                            if (++_n > 10) {
                                callback({
                                    status: false,
                                    error: 'CSS文件加载失败，请重试...',
                                    path: path
                                });
                            } else {
                                window.setTimeout(function() {
                                    _poll(node, callback);
                                }, 100);
                            }
                        }
                    };

                    _poll(node, callback);
                }
                ecpic.log(path + '加载完成');
            };
            node.onerror = function(e) {
                ecpic.log(path + '加载失败，失败原因是：' + (typeof e === 'string' ? e : JSON.stringify(e)));
                callback({
                    status: false,
                    error: (js ? 'script' : 'CSS') + '文件加载时发生错误，请重试...',
                    path: path
                });
            };
            el.appendChild(node);
        }
    };