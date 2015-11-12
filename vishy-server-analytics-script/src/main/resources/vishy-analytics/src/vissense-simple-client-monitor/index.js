/*global VisSense */
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
