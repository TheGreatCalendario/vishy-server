!function(a,b,c){"use strict";var d=a[b],e=c(a,a.document);a[b]=e,a[b].noConflict=function(){return a[b]=d,e}}(this,"VisSense",function(a,b,c){"use strict";function d(a,b){return function(){var d=arguments;return g(function(){a.apply(c,d)},b||0)}}function e(a,b){var c=q;return function(){var d=this,e=arguments;c(),c=g(function(){a.apply(d,e)},b)}}function f(a,b){var d=p(b),e=p(a);return d||e?d&&e?(j(Object.keys(b),function(d){a[d]===c&&(a[d]=b[d])}),a):d?b:a:b}function g(a,b){var c=setTimeout(function(){a()},b||0);return function(){clearTimeout(c)}}function h(a,b){return function(){return(o(a)?a():a)?b():c}}function i(a,b,c){for(var d=-1,e=Object.keys(b),f=e.length,g=o(c);++d<f;){var h=e[d];a[h]=g?c(a[h],b[h],h,a,b):b[h]}return a}function j(a,b,d){for(var e=0,f=a.length;f>e;e++){var g=b.call(d,a[e],e,a);if(g!==c)return g}}function k(a){return a}function l(a){return a!==c}function m(a){return a&&"object"==typeof a&&"number"==typeof a.length&&"[object Array]"===Object.prototype.toString.call(a)||!1}function n(a){return a&&1===a.nodeType||!1}function o(a){return"function"==typeof a||!1}function p(a){var b=typeof a;return"function"===b||a&&"object"===b||!1}function q(){}function r(){return(new Date).getTime()}function s(a){var b,d=!1;return function(){return d||(b=a.apply(c,arguments),d=!0),b}}function t(a,b,c){var d=q,e=!1;return function(){var f=r(),h=arguments,i=function(){e=f,a.apply(c,h)};e&&e+b>f?(d(),d=g(i,b)):(e=f,g(i,0))}}function u(b){var c=b||a;return{height:c.innerHeight,width:c.innerWidth}}function v(b,c){return(c||a).getComputedStyle(b,null)}function w(a,b){return a.getPropertyValue(b)}function x(a,b){b||(b=v(a));var c=w(b,"display");if("none"===c)return!1;var d=a.parentNode;return n(d)?x(d):!0}function y(b,c){if(b===(c||a).document)return!0;if(!b||!b.parentNode)return!1;var d=v(b,c),e=w(d,"visibility");return"hidden"===e||"collapse"===e?!1:x(b,d)}function z(a,b){return!a||a.width<=0||a.height<=0?!1:a.bottom>0&&a.right>0&&a.top<b.height&&a.left<b.width}function A(a,b){var c=a.getBoundingClientRect(),d=u(b);if(!z(c,d)||!y(a))return 0;var e=0,f=0;return c.top>=0?e=Math.min(c.height,d.height-c.top):c.bottom>0&&(e=Math.min(d.height,c.bottom)),c.left>=0?f=Math.min(c.width,d.width-c.left):c.right>0&&(f=Math.min(d.width,c.right)),Math.round(e*f/(c.height*c.width)*1e3)/1e3}function B(b){return!F(b||a).isHidden()}function C(b,c){if(!(this instanceof C))return new C(b,c);if(!n(b))throw new Error("not an element node");this._element=b,this._config=f(c,{fullyvisible:1,hidden:0,referenceWindow:a,percentageHook:A,visibilityHooks:[]});var d=F(this._config.referenceWindow);this._config.visibilityHooks.push(function(){return!d.isHidden()})}function D(a,b){var c=a.state(),d=c.percentage;return b&&d===b.percentage&&b.percentage===b.previous.percentage?b:c.hidden?C.VisState.hidden(d,b):c.fullyvisible?C.VisState.fullyvisible(d,b):C.VisState.visible(d,b)}function E(a,b){var c=f(b,{strategy:[new E.Strategy.PollingStrategy,new E.Strategy.EventStrategy],async:!1});this._visobj=a,this._state={},this._started=!1;var d="*#"+r();this._pubsub=new G({async:c.async,anyTopicName:d}),this._events=[d,"start","stop","update","hidden","visible","fullyvisible","percentagechange","visibilitychange"],this._strategy=new E.Strategy.CompositeStrategy(c.strategy),this._strategy.init(this),this._pubsub.on("update",function(a){var b=a._state.percentage,c=a._state.previous.percentage;b!==c&&a._pubsub.publish("percentagechange",[a,b,c])}),this._pubsub.on("update",function(a){a._state.code!==a._state.previous.code&&a._pubsub.publish("visibilitychange",[a])}),this._pubsub.on("visibilitychange",function(a){a._state.visible&&!a._state.previous.visible&&a._pubsub.publish("visible",[a])}),this._pubsub.on("visibilitychange",function(a){a._state.fullyvisible&&a._pubsub.publish("fullyvisible",[a])}),this._pubsub.on("visibilitychange",function(a){a._state.hidden&&a._pubsub.publish("hidden",[a])}),j(this._events,function(a){o(c[a])&&this.on(a,c[a])},this)}var F=function(b){return function(a,b){var c=function(a,b){return{property:a,event:b}},d="visibilitychange",e=[c("webkitHidden","webkit"+d),c("msHidden","ms"+d),c("mozHidden","moz"+d),c("hidden",d)],f=j(e,function(c){return a[c.property]!==b?{isHidden:function(){return!!a[c.property]||!1},onVisibilityChange:function(b){return a.addEventListener(c.event,b,!1),function(){a.removeEventListener(c.event,b,!1)}}}:void 0});return f||{isHidden:function(){return!1},onVisibilityChange:function(){return q}}}((b||a).document)},G=function(a){function b(a){this._cache={},this._onAnyCache=[],this._config=f(a,{async:!1,anyTopicName:"*"})}var c=function(a,b){j(a,function(a){a(b)})};return b.prototype.on=function(b,c){if(!o(c))return q;var e=function(b){return c.apply(a,b||[])},f=this._config.async?d(e):e,g=function(a,b,c){return function(){var c=b.indexOf(a);return c>-1?(b.splice(c,1),!0):!1}};return b===this._config.anyTopicName?(this._onAnyCache.push(f),g(f,this._onAnyCache,"*")):(this._cache[b]||(this._cache[b]=[]),this._cache[b].push(f),g(f,this._cache[b],b))},b.prototype.publish=function(a,b){var e=(this._cache[a]||[]).concat(a===this._config.anyTopicName?[]:this._onAnyCache),f=!!this._config.async,g=f?d(c):function(a,b){return c(a,b),q};return g(e,b||[])},b}();C.prototype.state=function(){var a=j(this._config.visibilityHooks,function(a){return a(this._element)?void 0:C.VisState.hidden(0)},this);return a||function(a,b){var c=b.percentageHook(a,b.referenceWindow);return c<=b.hidden?C.VisState.hidden(c):c>=b.fullyvisible?C.VisState.fullyvisible(c):C.VisState.visible(c)}(this._element,this._config)},C.prototype.percentage=function(){return this.state().percentage},C.prototype.element=function(){return this._element},C.prototype.referenceWindow=function(){return this._config.referenceWindow},C.prototype.isFullyVisible=function(){return this.state().fullyvisible},C.prototype.isVisible=function(){return this.state().visible},C.prototype.isHidden=function(){return this.state().hidden},C.fn=C.prototype,C.of=function(a,b){return new C(a,b)};var H={HIDDEN:[0,"hidden"],VISIBLE:[1,"visible"],FULLY_VISIBLE:[2,"fullyvisible"]};return C.VisState=function(){function a(a,b,c){return c&&delete c.previous,{code:a[0],state:a[1],percentage:b,previous:c||{},fullyvisible:a[0]===H.FULLY_VISIBLE[0],visible:a[0]===H.VISIBLE[0]||a[0]===H.FULLY_VISIBLE[0],hidden:a[0]===H.HIDDEN[0]}}return{hidden:function(b,c){return a(H.HIDDEN,b,c)},visible:function(b,c){return a(H.VISIBLE,b,c)},fullyvisible:function(b,c){return a(H.FULLY_VISIBLE,b,c)}}}(),E.prototype.visobj=function(){return this._visobj},E.prototype.publish=function(a,b){var c=this._events.indexOf(a)>=0;if(c)throw new Error('Cannot publish internal event "'+a+'" from external scope.');return this._pubsub.publish(a,b)},E.prototype.state=function(){return this._state},E.prototype.start=function(a){if(this._started)return this;var b=f(a,{async:!1});return this._cancelAsyncStart&&this._cancelAsyncStart(),b.async?this.startAsync():(this.update(),this._pubsub.publish("start",[this]),this._strategy.start(this),this._started=!0,this)},E.prototype.startAsync=function(a){this._cancelAsyncStart&&this._cancelAsyncStart();var b=this,c=g(function(){b.start(i(f(a,{}),{async:!1}))});return this._cancelAsyncStart=function(){c(),b._cancelAsyncStart=null},this},E.prototype.stop=function(){this._cancelAsyncStart&&this._cancelAsyncStart(),this._started&&(this._strategy.stop(this),this._pubsub.publish("stop",[this])),this._started=!1},E.prototype.update=function(){this._state=D(this._visobj,this._state),this._pubsub.publish("update",[this])},E.prototype.on=function(a,b){return this._pubsub.on(a,b)},E.Builder=function(){var a=function(a,b){var c=null,d=a.strategy===!1,e=!d&&(a.strategy||b.length>0);if(e){var f=!!a.strategy,g=m(a.strategy),h=f?g?a.strategy:[a.strategy]:[];c=h.concat(b)}else c=d?[]:a.strategy;return c};return function(b){var c={},d=[],e=[],f=!1,g=null;return{set:function(a,b){return c[a]=b,this},strategy:function(a){return d.push(a),this},on:function(a,b){return e.push([a,b]),this},build:function(h){var i=function(){var h=a(c,d);c.strategy=h;var i=b.monitor(c);return j(e,function(a){i.on(a[0],a[1])}),f=!0,g=i},k=f?g:i();return o(h)?h(k):k}}}}(),E.Strategy=function(){},E.Strategy.prototype.init=q,E.Strategy.prototype.start=q,E.Strategy.prototype.stop=q,E.Strategy.CompositeStrategy=function(a){this._strategies=m(a)?a:[a]},E.Strategy.CompositeStrategy.prototype=Object.create(E.Strategy.prototype),E.Strategy.CompositeStrategy.prototype.init=function(a){j(this._strategies,function(b){o(b.init)&&b.init(a)})},E.Strategy.CompositeStrategy.prototype.start=function(a){j(this._strategies,function(b){o(b.start)&&b.start(a)})},E.Strategy.CompositeStrategy.prototype.stop=function(a){j(this._strategies,function(b){o(b.stop)&&b.stop(a)})},E.Strategy.PollingStrategy=function(a){this._config=f(a,{interval:1e3}),this._started=!1},E.Strategy.PollingStrategy.prototype=Object.create(E.Strategy.prototype),E.Strategy.PollingStrategy.prototype.start=function(a){return this._started||(this._clearInterval=function(b){var c=setInterval(function(){a.update()},b);return function(){clearInterval(c)}}(this._config.interval),this._started=!0),this._started},E.Strategy.PollingStrategy.prototype.stop=function(){return this._started?(this._clearInterval(),this._started=!1,!0):!1},E.Strategy.EventStrategy=function(a){this._config=f(a,{throttle:50}),this._config.debounce>0&&(this._config.throttle=+this._config.debounce),this._started=!1},E.Strategy.EventStrategy.prototype=Object.create(E.Strategy.prototype),E.Strategy.EventStrategy.prototype.start=function(a){return this._started||(this._removeEventListeners=function(b){var c=F(a.visobj().referenceWindow()),d=c.onVisibilityChange(b);return addEventListener("scroll",b,!1),addEventListener("resize",b,!1),addEventListener("touchmove",b,!1),function(){removeEventListener("touchmove",b,!1),removeEventListener("resize",b,!1),removeEventListener("scroll",b,!1),d()}}(t(function(){a.update()},this._config.throttle)),this._started=!0),this._started},E.Strategy.EventStrategy.prototype.stop=function(){return this._started?(this._removeEventListeners(),this._started=!1,!0):!1},C.VisMon=E,C.PubSub=G,C.fn.monitor=function(a){return new E(this,a)},C.Utils={async:d,debounce:e,defaults:f,defer:g,extend:i,forEach:j,fireIf:h,identity:k,isArray:m,isDefined:l,isElement:n,isFunction:o,isObject:p,isPageVisible:B,isVisibleByStyling:y,noop:q,now:r,once:s,throttle:t,percentage:A,VisibilityApi:F(),createVisibilityApi:F,_viewport:u,_isInViewport:z,_isDisplayed:x,_computedStyle:v,_styleProperty:w},C});;/*! { "name": "vissense-percentage-time-test", "version": "0.5.0", "copyright": "(c) 2015 tbk" } */
!function(a,b){"use strict";b(a,a.VisSense,a.VisSense.Utils)}(this,function(a,b,c,d){"use strict";var e=function(a,d,e){var f=0,g=null,h=e.timeLimit,i=e.percentageLimit,j=e.interval;return b.VisMon.Builder(a.visobj()).strategy(new b.VisMon.Strategy.PollingStrategy({interval:j})).on("update",function(b){var e=b.state().percentage;if(i>e)g=null;else{var j=c.now();g=g||j,f=j-g}f>=h&&(b.stop(),a.stop(),d(b))}).on("stop",function(){g=null}).build()},f=function(a,f,g){var h=c.defaults(g,{percentageLimit:1,timeLimit:1e3,interval:100,strategy:d}),i=Math.max(h.percentageLimit-.001,0),j=null,k=b.VisMon.Builder(new b(a.element(),{hidden:i,referenceWindow:a.referenceWindow()})).set("strategy",h.strategy).on("visible",function(a){null===j&&(j=e(a,f,h)),j.start()}).on("hidden",function(){null!==j&&j.stop()}).on("stop",function(){null!==j&&j.stop()}).build();return k.start(),function(){k.stop(),j=null}};b.fn.onPercentageTimeTestPassed=function(a,b){f(this,a,b)},b.fn.on50_1TestPassed=function(a,b){var d=c.extend(c.defaults(b,{interval:100}),{percentageLimit:.5,timeLimit:1e3});f(this,a,d)},b.VisMon.Strategy.PercentageTimeTestEventStrategy=function(a,b){var d=function(b,d){var e=c.noop,g=b.on("visible",c.once(function(b){e=f(b.visobj(),function(c){var e={monitorState:c.state(),testConfig:d};b.update(),b.publish(a,[b,e])},d),g()}));return function(){g(),e()}},e=c.noop;return{init:function(a){e=d(a,b)},stop:function(){e()}}}});;/*! { "name": "vissense-metrics", "version": "0.1.0", "copyright": "(c) 2015 tbk" } */
!function(a){"use strict";!function(a){Date.now||(Date.now=function(){return(new Date).getTime()}),a.performance||(a.performance=a.performance||{},a.performance.now=a.performance.now||a.performance.mozNow||a.performance.msNow||a.performance.oNow||a.performance.webkitNow||Date.now)}(a);var b=function(){function a(b){return this instanceof a?((+b!==b||0>b)&&(b=0),void(this._$={i:b})):new a(b)}var b=Math.pow(2,32),c=function(a){return+a!==a?1:+a};return a.MAX_VALUE=b,a.prototype.inc=function(a){return this.set(this.get()+c(a)),this.get()},a.prototype.dec=function(a){return this.inc(-1*c(a))},a.prototype.clear=function(){var a=this._$.i;return this._$.i=0,a},a.prototype.get=function(){return this._$.i},a.prototype.set=function(a){return this._$.i=c(a),this._$.i<0?this._$.i=0:this._$.i>b&&(this._$.i-=b),this.get()},a}(),c=function(){function b(a){return this instanceof b?(this._config=a||{},this._config.performance=this._config.performance===!0,void(this._$={ts:0,te:0,r:!1})):new b(a)}var c=function(b){return b?a.performance.now():Date.now()},d=function(a,b){return+a===a?+a:b};return b.prototype._orNow=function(a){return d(a,c(this._config.performance))},b.prototype.startIf=function(a,b){return a&&(this._$.r=!0,this._$.ts=this._orNow(b),this._$.te=null),this},b.prototype.start=function(a){return this.startIf(!this._$.r,a)},b.prototype.restart=function(a){return this.startIf(!0,a)},b.prototype.stop=function(a){return this.stopIf(!0,a)},b.prototype.stopIf=function(a,b){return this._$.r&&a&&(this._$.te=this._orNow(b),this._$.r=!1),this},b.prototype.interim=function(a){return this._$.r?this._orNow(a)-this._$.ts:0},b.prototype.get=function(a){return this._$.te?this._$.te-this._$.ts:this.time(a)},b.prototype.running=function(){return this._$.r},b.prototype.getAndRestartIf=function(a,b){var c=this.get(b);return a&&this.restart(b),c},b.prototype.forceStart=b.prototype.restart,b.prototype.time=b.prototype.interim,b}();a.CountOnMe={counter:b,stopwatch:c}}(window),function(a,b,c,d){"use strict";function e(a,b){a>0&&b(a)}function f(){this.metrics={},this.addMetric=function(a,b){this.metrics[a]=b},this.getMetric=function(a){return this.metrics[a]}}function g(a){function b(a){var b=a.state(),c=b.percentage;h.getMetric("percentage").set(c),c<h.getMetric("percentage.min").get()&&h.getMetric("percentage.min").set(c),c>h.getMetric("percentage.max").get()&&h.getMetric("percentage.max").set(c)}function c(a){var b=a.state();e(l.get(),function(a){h.getMetric("time.duration").set(a)}),e(k.running()?k.stop().get():-1,function(a){h.getMetric("time.hidden").inc(a)}),e(i.running()?i.stop().get():-1,function(a){h.getMetric("time.visible").inc(a),h.getMetric("time.relativeVisible").inc(a*b.percentage)}),e(j.running()?j.stop().get():-1,function(a){h.getMetric("time.fullyvisible").inc(a)}),i.startIf(b.visible),j.startIf(b.fullyvisible),k.startIf(b.hidden),l.startIf(!l.running())}var g=!1,h=new f,i=d.stopwatch(),j=d.stopwatch(),k=d.stopwatch(),l=d.stopwatch();h.addMetric("time.visible",new d.counter),h.addMetric("time.fullyvisible",new d.counter),h.addMetric("time.hidden",new d.counter),h.addMetric("time.relativeVisible",new d.counter),h.addMetric("time.duration",new d.counter),h.addMetric("percentage",new d.counter),h.addMetric("percentage.max",new d.counter(0)),h.addMetric("percentage.min",new d.counter(1));var m=this;this.update=function(){c(a),b(a)},this.getMetric=function(a){return h.getMetric(a)},this.running=function(){return g};var n=null;this.start=function(){return g||(n=a.on("update",function(){m.update()}),this.update(),g=!0),this},this.stop=function(){return g&&(this.update(),n(),n=null,g=!1),this}}var h=b.VisMon.Strategy;h.MetricsStrategy=function(){},h.MetricsStrategy.prototype=Object.create(h.prototype),h.MetricsStrategy.prototype.init=function(a){var b=new g(a);a.metrics=function(){return b}},h.MetricsStrategy.prototype.start=function(a){return a.metrics().start(),!0},h.MetricsStrategy.prototype.stop=function(a){return a.metrics().stop(),!0}}.call(this,window,window.VisSense,window.VisSense.Utils,window.CountOnMe);;/*! { "name": "vissense-user-activity", "version": "0.3.0", "copyright": "(c) 2015 tbk" } */!function(a,b){"use strict";b(a,a.VisSense)}(this,function(a,b,c){"use strict";function d(b,c){j(b,function(b){b.call(c||a)})}function e(b){if(!(this instanceof e))return new e(b);this._config=i(b,{inactiveAfter:6e4,throttle:100,events:["resize","scroll","mousemove","mousewheel","keydown","mousedown","touchstart","touchmove"],active:l,inactive:l,update:l,referenceWindow:a}),this._listeners=[],this._cancelUpdate=l,this._state={changed:!0,active:!1,lastActivityTime:m(),started:!1},this._visibilityApi=f.createVisibilityApi(this._config.referenceWindow);var c=this;this._updateState=function(){var a=c._state.active,b=c.getTimeSinceLastActivity();c._visibilityApi.isHidden()||b>=c._config.inactiveAfter?c._state.active=!1:(c._state.active=!0,c._cancelUpdate(),c._cancelUpdate=h(function(){c._updateState()},c._config.inactiveAfter)),c._state.changed=a!==c._state.active,d(c._listeners,c)},this._onUserActivity=function(){c._state.lastActivityTime=m(),c._updateState()},this.onUpdate(this._config.update),this.onActive(this._config.active),this.onInactive(this._config.inactive)}var f=b.Utils,g=f.throttle,h=f.defer,i=f.defaults,j=f.forEach,k=f.isFunction,l=f.noop,m=f.now,n=b.VisMon.Strategy,o=function(a,b){var c=a.indexOf(b);return c>-1?(a.splice(c,1),!0):!1};e.prototype.start=function(){return this._state.started?this:(this._removeEventListeners=function(a,b,c){var d=c.referenceWindow,e=g(b,c.throttle),f=a.onVisibilityChange(e),h=c.events;return j(h,function(a){d.addEventListener(a,e,!1)}),function(){j(h,function(a){d.removeEventListener(a,e,!1)}),f()}}(this._visibilityApi,this._onUserActivity,this._config),this._state.started=!0,this._onUserActivity(),this)},e.prototype.stop=function(){return this._state.started?(this._removeEventListeners(),this._cancelUpdate(),this._state.started=!1,this):this},e.prototype.onUpdate=function(a){if(!k(a))return l;var b=a.bind(c,this);this._listeners.push(b);var d=this;return function(){return o(d._listeners,b)}},e.prototype.onChange=function(a){return this.onUpdate(function(b){b._state.changed&&a(b)})},e.prototype.onActive=function(a){return this.onChange(function(b){b._state.active&&a(b)})},e.prototype.onInactive=function(a){return this.onChange(function(b){b._state.active||a(b)})},e.prototype.isActive=function(){return!this._state.started||this._state.active},e.prototype.getTimeSinceLastActivity=function(){return m()-this._state.lastActivityTime},b.UserActivity=e,n.UserActivityStrategy=function(a){this._userActivity=new e(a);var b=this;this._visibilityHook=function(){return b._userActivity.isActive()}},n.UserActivityStrategy.prototype=Object.create(n.prototype),n.UserActivityStrategy.prototype.init=function(a){this._removeVisibilityHook=function(b){var c=a.visobj()._config.visibilityHooks;return c.push(b._visibilityHook),function(){o(c,b._visibilityHook)}}(this),this._removeOnChangeListener=this._userActivity.onChange(function(){a.update()})},n.UserActivityStrategy.prototype.start=function(){this._userActivity.start()},n.UserActivityStrategy.prototype.stop=function(){this._removeVisibilityHook(),this._removeOnChangeListener(),this._userActivity.stop()}});;/*global VisSense */
(function (VisSense, Utils) {
  'use strict';

  // FIXME: temporary -> Everything in here should be considered to be moved to an own plugin

  var simpleHelpers = function () {
  };

  simpleHelpers.createTimeReport = function (metrics) {
    var report = {};
    report.timeHidden = metrics.getMetric('time.hidden').get();
    report.timeVisible = metrics.getMetric('time.visible').get();
    report.timeFullyVisible = metrics.getMetric('time.fullyvisible').get();
    report.timeRelativeVisible = Math.round(metrics.getMetric('time.relativeVisible').get());
    report.duration = metrics.getMetric('time.duration').get();
    report.timeStarted = new Date().getTime() - report.duration;

    report.percentage = {
      current: metrics.getMetric('percentage').get(),
      maximum: metrics.getMetric('percentage.max').get(),
      minimum: metrics.getMetric('percentage.min').get()
    };

    return report;
  };

  simpleHelpers.newInitialStateEventStrategy = function (eventName) {
    return {
      init: function (monitor) {
        console.debug('[InitialRequestEventStrategy] init');
        var stopSendingInitialRequestEvents = monitor.on('update', function (monitor) {
          var state = monitor.state();
          monitor.publish(eventName, [monitor, state]);
          stopSendingInitialRequestEvents();
        });
      }
    };
  };

  simpleHelpers.createSummaryEventStrategy = function (eventName) {
    return {
      init: function (monitor) {
        if (!Utils.isFunction(monitor.metrics) || !Utils.isFunction(VisSense.VisMon.Strategy.MetricsStrategy)) {
          throw new Error('Cannot load MetricsStrategy. Is it included?');
        }
      },
      start: function () {
        console.debug('[TimeReportEventStrategy] start');
      },
      stop: function (monitor) {
        monitor.metrics().update();

        var timeReport = simpleHelpers.createTimeReport(monitor.metrics());

        monitor.publish(eventName, [monitor, timeReport]);

        console.debug('[TimeReportEventStrategy] stop');
      }
    };
  };

  VisSense.Client = VisSense.Client || {};
  VisSense.Client.Helpers = VisSense.Client.Helpers || {};
  VisSense.Client.Helpers.Simple = simpleHelpers;

})(VisSense, VisSense.Utils);

(function (VisSense, factory) {
  'use strict';

  VisSense.Client = VisSense.Client || {};
  VisSense.Client.Simple = factory(VisSense, VisSense.Utils);
})(VisSense, function (VisSense, Utils) {
  'use strict';

  var uuid = function () {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  };

  var viewportSize = function () {
    var width = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
    var height = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    return {
      width: width,
      height: height
    };
  };

  var sessionId = uuid();

  var addStandardProperties = function (data) {
    return Utils.extend(data, {
      sessionId: sessionId,
      viewport: viewportSize()
    });
  };

  var sendEventWithClient = function (client, eventCollection, event) {
    var data = addStandardProperties(event);

    client.addEvent(eventCollection, data, function (err/*, res*/) {
      if (err) {
        // there was an error!
        console.log('error on event ' + eventCollection);
      }
      else {
        // see sample response below
        console.info('successfully sent event ' + eventCollection);
      }
    });
  };

  return function () {
    if (!Utils.isFunction(VisSense.VisMon.Strategy.MetricsStrategy)) {
      throw new Error('Cannot load MetricsStrategy. Is it included?');
    }

    return {
      monitors: function (externalClient) {
        var client = {
          addEvent: function (eventCollection, data, consumer) {
            externalClient.addEvent(eventCollection, data, consumer);
          }
        };

        return {
          standard: function (visobj) {
            return this.newBuilder(visobj).build();
          },
          custom: function (visobj, options) {
            return this.newBuilder(visobj, options).build();
          },
          newBuilder: function (visobj, options) {
            var config = Utils.defaults(options, {
              interval: 1000,
              throttle: 100,
              inactiveAfter: 60000
            });

            var builder = VisSense.VisMon.Builder(visobj)
              .strategy(new VisSense.VisMon.Strategy.PollingStrategy({interval: config.interval}))
              .strategy(new VisSense.VisMon.Strategy.EventStrategy({throttle: config.throttle}))
              .strategy(new VisSense.VisMon.Strategy.UserActivityStrategy({
                inactiveAfter: config.inactiveAfter
              }));

            return this.prepareBuilder(builder);
          },
          prepareBuilder: function (builder) {
            var monitorId = uuid();

            var r = monitorId.substring(0, 7);
            var asInternalEventName = function (eventName) {
              return r + '#' + eventName;
            };

            var initEventName = 'visibility-initial-request';
            var status501TestPassedEventName = 'visibility-status-50/1';
            var summaryEventName = 'visibility-time-report';

            var internalInitEventName = asInternalEventName(initEventName);
            var internalStatus501TestPassedEventName = asInternalEventName(status501TestPassedEventName);
            var internalSummaryEventName = asInternalEventName(summaryEventName);

            return builder
              .strategy(new VisSense.VisMon.Strategy.MetricsStrategy())
              .strategy(VisSense.Client.Helpers.Simple.newInitialStateEventStrategy(internalInitEventName))
              .strategy(VisSense.VisMon.Strategy.PercentageTimeTestEventStrategy(internalStatus501TestPassedEventName, {
                percentageLimit: 0.5,
                timeLimit: 1000,
                interval: 100
              }))
              .strategy(VisSense.Client.Helpers.Simple.createSummaryEventStrategy(internalSummaryEventName))
              .on(internalInitEventName, function (monitor, state) {
                var stateWithoutPrevious = Utils.extend({}, state);
                stateWithoutPrevious.previous = null;

                var initEventData = {
                  type: 'INITIAL',
                  monitorId: monitorId,
                  initial: {
                    timeStarted: new Date().getTime(),
                    state: stateWithoutPrevious
                  }
                };

                sendEventWithClient(client, 'visibility-initial-request', initEventData);

                monitor.publish(initEventName, [monitor, initEventData]);
              })
              .on(internalStatus501TestPassedEventName, function (monitor, data) {
                var timeReport = VisSense.Client.Helpers.Simple.createTimeReport(monitor.metrics());

                if (data.monitorState) {
                  var stateWithoutPrevious = Utils.extend({}, data.monitorState);
                  stateWithoutPrevious.previous = null;
                  data.monitorState = stateWithoutPrevious;
                }

                var dataWithTimeReport = Utils.extend(data, {
                  timeReport: timeReport
                });

                var status501TestPassedEventData = {
                  type: 'STATUS',
                  monitorId: monitorId,
                  status: {
                    test: dataWithTimeReport
                  }
                };

                sendEventWithClient(client, 'visibility-percentage-time-test', status501TestPassedEventData);
                monitor.publish(status501TestPassedEventName, [monitor, status501TestPassedEventData]);
              })
              .on(internalSummaryEventName, function (monitor, timeReport) {
                var summaryEventData = {
                  type: 'SUMMARY',
                  monitorId: monitorId,
                  summary: {
                    report: timeReport
                  }
                };

                sendEventWithClient(client, 'visibility-time-report', summaryEventData);
                monitor.publish(summaryEventName, [monitor, summaryEventData]);
              });
          }
        };

      }
    };
  };
});
;/*global VisSense */
(function (VisSense, Utils) {
  'use strict';

  VisSense.Client = VisSense.Client || {};

  var createBaseEndpoint = function (protocol, host, port) {
    return protocol + '://' + host + ':' + port + '';
  };

  VisSense.Client.Vishy = function(config, http) {
    if (!Utils.isFunction(VisSense.Client.Simple)) {
      throw new Error('Cannot load VisSense.Client.Simple. Is it included?');
    }

    var vishyConfig = Utils.defaults(config, {
      protocol: 'http',
      host: 'api.vishy.io',
      port: 80
    });

    if (!http || !http.post) {
      throw new Error('Please provide a compatible http client!');
    }

    var baseEndpoint = createBaseEndpoint(vishyConfig.protocol, vishyConfig.host, vishyConfig.port);

    return {
      monitors: function(config) {
        if (!config.projectId) {
          throw new Error('Please provide a vishy.projectId!');
        }
        if (!config.elementId) {
          throw new Error('Please provide a vishy.projectId!');
        }

        var vishyObject = {
          elementId: config.elementId,
          projectId: config.projectId
        };

        var client = {
          addEvent: function (eventCollection, data, consumer) {
            var url = baseEndpoint + '/openmrc/consume';

            var _data = Utils.extend(data, {
              vishy: vishyObject
            });

            http.post(url, _data, {}).then(function (data) {
              consumer(null, data);
            }, function (error) {
              consumer(error);
            });
          }
        };

        return {
          standard: function (visobj) {
            return VisSense.Client.Simple().monitors(client).standard(visobj);
          },
          custom: function (visobj, options) {
            return VisSense.Client.Simple().monitors(client).custom(visobj, options);
          },
          newBuilder: function (visobj, options) {
            return VisSense.Client.Simple()
              .monitors(client)
              .newBuilder(visobj, options);
          }
        };
      }
    };
  };


})(VisSense, VisSense.Utils);
;(function (window, document, VishyObjectName, VisSense, $http) {
  'use strict';
  var vishy = window[VishyObjectName] || {};

  var jsonHttp = {
    post: function (src, data) {
      return $http.post(src, JSON.stringify(data));
    }
  };

  var newMonitor = function (VisSense, elementId, config) {
    var element = document.getElementById(elementId);
    var visobj = VisSense.of(element, {
      hidden: 0.49
    });
    var monitor = VisSense.Client.Vishy(config, jsonHttp)
      .monitors({
        projectId: config.projectId,
        elementId: elementId
      }).custom(visobj, {
        interval: 1000,
        throttle: 100,
        inactiveAfter: 60 * 1000
      });

    monitor.on('visibility-initial-request', function (monitor, data) {
      console.table(data);
    });
    monitor.on('visibility-status-50/1', function (monitor, data) {
      console.table(data);
    });
    monitor.on('visibility-time-report', function (monitor, data) {
      console.table(data);
    });

    (function registerStopOnBeforeUnloadEventHandler() {
      var removeBeforeUnloadStopCallback = function () {
      };
      monitor.on('start', function () {
        removeBeforeUnloadStopCallback = (function () {
          var eventId = addEventListener('beforeunload', function () {
            monitor.stop();
          }, true);

          return function () {
            removeEventListener('beforeunload', eventId, true);
          };
        })();
      });
      monitor.on('stop', function () {
        removeBeforeUnloadStopCallback();
      });
    }());

    return monitor;
  };

  var createMonitor = function (VisSense, elementId, config) {
    var Utils = VisSense.Utils;
    var vishyConfig = Utils.defaults(config, {
      readKey: '%%vishy.readKey%%',
      writeKey: '%%vishy.writeKey%%',
      protocol: 'http',
      host: 'localhost',
      port: 8080,
      requestType: 'xhr'
    });

    return newMonitor(VisSense, elementId, vishyConfig);
  };

  var setup = function (VisSense) {
    var Utils = VisSense.Utils;
    var monitors = [];
    var commandQueue = vishy.q || [];

    Utils.forEach(commandQueue, function (args) {
      if (Utils.isArray(args) || args.length === 0) {
        console.log('Illegal arguments.', args);
        return;
      }

      var action = args[0];
      if (action === 'init') {
        console.log('Unimplemented operation.', 'init');
      }
      else if (action === 'create') {
        try {
          var monitor = createMonitor(VisSense, args[1], args[2]);
          monitors.push(monitor);
        } catch (e) {
          console.log('MonitorCreationError', e);
        }
      }
      else if (action === 'start') {
        try {
          var callback = Utils.isFunction(args[1]) ? args[1] : Utils.noop;
          Utils.forEach(monitors, function (monitor) {
            monitor.start();
            callback(monitor);
          });
          monitors = [];
        } catch (e) {
          console.log('MonitorStartError', e);
        }
      }
      else {
        console.log('Unsupported operation.', args);
      }
    });
  };


  setup(VisSense);

}(window, window.document, window.VishyObject || 'vishy', window.VisSense.noConflict(), (function () {
  'use strict';
  var noop = function () {
  };
  var asParamsArray = function (req) {
    return [req, req.responseText];
  };

  var xhr = function (type, url, data) {
    var methods = {
      success: noop,
      error: noop
    };
    var request = new XMLHttpRequest();
    request.withCredentials = true;
    request.onreadystatechange = function () {
      if (request.readyState === 4) {
        if (request.status >= 200 && request.status < 400) {
          methods.success.apply(methods, asParamsArray(request));
        } else {
          methods.error.apply(methods, asParamsArray(request));
        }
      }
    };
    request.open(type, url, true);

    request.send(data);

    return {
      then: function (success, error) {
        methods.success = success;
        methods.error = error;
        return methods;
      },
      success: function (callback) {
        methods.success = callback;
        return methods;
      },
      error: function (callback) {
        methods.error = callback;
        return methods;
      }
    };
  };

  return {
    get: function (src) {
      return xhr('GET', src, null);
    },
    post: function (src, data) {
      return xhr('POST', src, data);
    }
  };
}())));
