(function (window, document, VishyObjectName, VisSense, $http) {
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
