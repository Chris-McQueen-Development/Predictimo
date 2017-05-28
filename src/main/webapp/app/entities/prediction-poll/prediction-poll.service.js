(function() {
    'use strict';
    angular
        .module('predictimoApp')
        .factory('PredictionPoll', PredictionPoll);

    PredictionPoll.$inject = ['$resource', 'DateUtils'];

    function PredictionPoll ($resource, DateUtils) {
        var resourceUrl =  'api/prediction-polls/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.pollEndDate = DateUtils.convertLocalDateFromServer(data.pollEndDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.pollEndDate = DateUtils.convertLocalDateToServer(copy.pollEndDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.pollEndDate = DateUtils.convertLocalDateToServer(copy.pollEndDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
