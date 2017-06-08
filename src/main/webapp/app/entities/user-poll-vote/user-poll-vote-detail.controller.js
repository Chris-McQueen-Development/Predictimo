(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('UserPollVoteDetailController', UserPollVoteDetailController);

    UserPollVoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserPollVote', 'UserProfile', 'PredictionResponse'];

    function UserPollVoteDetailController($scope, $rootScope, $stateParams, previousState, entity, UserPollVote, UserProfile, PredictionResponse) {
        var vm = this;

        vm.userPollVote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('predictimoApp:userPollVoteUpdate', function(event, result) {
            vm.userPollVote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
