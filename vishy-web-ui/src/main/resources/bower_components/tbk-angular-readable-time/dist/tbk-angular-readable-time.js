(function (angular) {
  var second = 1000;
  var minute = second * 60;
  var hour = minute * 60;
  var day = hour * 24;
  var week = day * 7;
  var year = day * 365;
  var month = year / 12;

  angular.module('tbk.readableTime.config', [])
    .value('tbkReadableTimeConfig', {
      debug: true,
      time: {
        second: second,
        minute: minute,
        hour: hour,
        day: day,
        week: week,
        month: month,
        year: year
      }
    });

  angular.module('tbk.readableTime.directives', []);
  angular.module('tbk.readableTime.filters', [
    'tbk.readableTime.config'
  ]);
  angular.module('tbk.readableTime', [
    'tbk.readableTime.config',
    'tbk.readableTime.directives',
    'tbk.readableTime.filters'
  ]);

})(angular);

(function (angular) {

  angular.module('tbk.readableTime.directives')
    .directive('tbkReadableTimeCountdown', [function () {
      var d = {
        scope: {
          start: '@',
          interval: '@',
          minUnit: '@',
          reverse: '@'
        },
        controller: ['$scope', '$interval', function ($scope, $interval) {
          var factor = !!$scope.reverse ? -1 : 1;
          var value = function () {
            return ($scope.start - new Date().getTime()) * factor;
          };

          $scope.milliseconds = value();
          $interval(function () {
            $scope.milliseconds = value();
          }, $scope.interval || 1000);
        }],
        template: '<span>{{ milliseconds | tbkReadableTimeRecursive:minUnit }}</span>'
      };

      return d;
    }])
  ;

})(angular);

(function (angular) {

  angular.module('tbk.readableTime.directives')
    .directive('tbkReadableTimeCountup', [function () {
      var d = {
        scope: {
          start: '@',
          interval: '@',
          minUnit: '@',
          reverse: '@'
        },
        controller: ['$scope', function ($scope) {
          $scope.reverse = !!$scope.reverse || false;
        }],
        template: '<span tbk-readable-time-countdown' +
        ' data-start="{{start}}" ' +
        ' data-interval="{{interval}}" ' +
        ' data-min-unit="{{minUnit}}" ' +
        ' data-reverse="{{!reverse}}"></span>'
      };

      return d;
    }])
  ;

})(angular);

(function (angular) {

  angular.module('tbk.readableTime.filters')

    .filter('tbkReadableTime', [
      '$injector', '$filter', 'tbkReadableTimeConfig',
      function ($injector, $filter, tbkReadableTimeConfig) {
        var time = tbkReadableTimeConfig.time;
        var format = function (number, label) {
          var text = label;
          if (number !== 1) {
            text += 's';
          }
          if ($injector.has('translateFilter')) {
            var translateText = '{{num}} ' + text;
            return $filter('translate')(translateText, {num: number});
          }

          return number + ' ' + text;
        };
        var round = function (number, scale) {
          var factor = Math.pow(10, scale >= 0 ? scale : 1);
          return Math.round(number * factor) / factor;
        };

        return function (milliseconds, scale) {
          var scaleOrZero = scale > 0 ? scale : 0;
          milliseconds = Math.round(milliseconds);

          switch (false) {
            case milliseconds >= time.second:
              return format(round(milliseconds, scaleOrZero), 'millisecond');
            case milliseconds >= time.minute:
              return format(round(milliseconds / time.second, scaleOrZero), 'second');
            case milliseconds >= time.hour:
              return format(round(milliseconds / time.minute, scaleOrZero), 'minute');
            case milliseconds >= time.day:
              return format(round(milliseconds / time.hour, scaleOrZero), 'hour');
            case milliseconds >= time.week:
              return format(round(milliseconds / time.day, scaleOrZero), 'day');
            case milliseconds >= time.month:
              return format(round(milliseconds / time.week, scaleOrZero), 'week');
            case milliseconds >= time.year:
              return format(round(milliseconds / time.month, scaleOrZero), 'month');
            default:
              return format(round(milliseconds / time.year, scaleOrZero), 'year');
          }
        };
      }])
    .filter('tbkReadableTimeRecursive', [
      '$filter', 'tbkReadableTimeFilter', 'tbkReadableTimeConfig',
      function ($filter, tbkReadableTime, tbkReadableTimeConfig) {
        var time = tbkReadableTimeConfig.time;
        return function (timeInMilliseconds, minUnit) {
          var options = {
            minMilliseconds: time[minUnit] > 0 ? time[minUnit] : 0
          };

          var self = $filter('tbkReadableTimeRecursive');

          var makeRecursiveCall = function (millisecons, scale) {
            var delta = Math.floor(milliseconds / scale);
            var rest = milliseconds - delta * scale;

            var lastStep = rest <= options.minMilliseconds || delta === 0;
            return tbkReadableTime(milliseconds - rest, 0) + (lastStep ? '' : ' ' + self(rest, minUnit));
          };

          var milliseconds = Math.round(timeInMilliseconds);

          switch (false) {
            case milliseconds >= time.second:
              return tbkReadableTime(milliseconds, 0);
            case milliseconds >= time.minute:
              return makeRecursiveCall(milliseconds, time.second);
            case milliseconds >= time.hour:
              return makeRecursiveCall(milliseconds, time.minute);
            case milliseconds >= time.day:
              return makeRecursiveCall(milliseconds, time.hour);
            case milliseconds >= time.week:
              return makeRecursiveCall(milliseconds, time.day);
            case milliseconds >= time.month:
              return makeRecursiveCall(milliseconds, time.week);
            case milliseconds >= time.year:
              return makeRecursiveCall(milliseconds, time.month);
            default:
              return makeRecursiveCall(milliseconds, time.year);
          }
        };
      }])
  ;

})(angular);
