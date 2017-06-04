(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('UserProfileDetailController', UserProfileDetailController);

    UserProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserProfile', 'User', 'UserPollVote', 'Prediction'];

    function UserProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, UserProfile, User, UserPollVote, Prediction) {
        var vm = this;

        vm.userProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('predictimoApp:userProfileUpdate', function(event, result) {
            vm.userProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
