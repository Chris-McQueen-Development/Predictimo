(function() {
    'use strict';
    angular
        .module('predictimoApp')
        .factory('PredictionPoll', PredictionPoll);

    PredictionPoll.$inject = ['$resource'];

    function PredictionPoll ($resource) {
        var resourceUrl =  'api/prediction-polls/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
