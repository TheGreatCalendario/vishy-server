angular.module('tbk.githubVersion', [])
  .directive('tbkGithubVersion', function() {
    var d = {
      scope: {
        repo: '@tbkGithubVersion'
      },
      template: '<span>{{version}}</span>',
      controller: ['$scope', '$http', function($scope, $http) {
        $scope.version = '?';
        $http.get('https://api.github.com/repos/' + $scope.repo + '/git/refs/tags', {
          cache: true
        }).success(function(data) {
          if(angular.isArray(data) && data.length > 0) {
            var latest = data[data.length - 1];
            var versionIndex = latest.ref.search(/(\d(\.)?){3}.*$/);
            if(versionIndex > -1) {
              var version = latest.ref.substring(versionIndex, latest.ref.length);
              $scope.version = version;
            }
          }
        });
      }],
      link: function($scope, $element) {
        $element.addClass('tbk-github-version');
      }
    };

    return d;
  });