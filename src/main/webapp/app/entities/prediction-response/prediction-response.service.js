(function() {
    'use strict';
    angular
        .module('predictimoApp')
        .factory('PredictionResponse', PredictionResponse);

    PredictionResponse.$inject = ['$resource', 'DateUtils'];

    function PredictionResponse ($resource, DateUtils) {
        var resourceUrl =  'api/prediction-responses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.responseDate = DateUtils.convertLocalDateFromServer(data.responseDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.responseDate = DateUtils.convertLocalDateToServer(copy.responseDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.responseDate = DateUtils.convertLocalDateToServer(copy.responseDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
