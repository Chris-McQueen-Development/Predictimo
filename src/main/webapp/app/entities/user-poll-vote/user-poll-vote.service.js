(function() {
    'use strict';
    angular
        .module('predictimoApp')
        .factory('UserPollVote', UserPollVote);

    UserPollVote.$inject = ['$resource'];

    function UserPollVote ($resource) {
        var resourceUrl =  'api/user-poll-votes/:id';

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
