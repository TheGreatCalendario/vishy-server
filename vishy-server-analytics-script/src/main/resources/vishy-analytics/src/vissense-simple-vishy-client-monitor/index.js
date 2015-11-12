/*global VisSense */
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
