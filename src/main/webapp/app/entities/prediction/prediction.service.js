(function() {
    'use strict';
    angular
        .module('predictimoApp')
        .factory('Prediction', Prediction);

    Prediction.$inject = ['$resource', 'DateUtils'];

    function Prediction ($resource, DateUtils) {
        var resourceUrl =  'api/predictions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.predictionCreatedDate = DateUtils.convertLocalDateFromServer(data.predictionCreatedDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.predictionCreatedDate = DateUtils.convertLocalDateToServer(copy.predictionCreatedDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.predictionCreatedDate = DateUtils.convertLocalDateToServer(copy.predictionCreatedDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
