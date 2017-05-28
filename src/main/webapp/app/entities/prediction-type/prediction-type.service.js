(function() {
    'use strict';
    angular
        .module('predictimoApp')
        .factory('PredictionType', PredictionType);

    PredictionType.$inject = ['$resource', 'DateUtils'];

    function PredictionType ($resource, DateUtils) {
        var resourceUrl =  'api/prediction-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.typeExpirationDate = DateUtils.convertLocalDateFromServer(data.typeExpirationDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.typeExpirationDate = DateUtils.convertLocalDateToServer(copy.typeExpirationDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.typeExpirationDate = DateUtils.convertLocalDateToServer(copy.typeExpirationDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
