(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionDetailController', PredictionDetailController);

    PredictionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Prediction', 'PredictionType', 'UserProfile', 'UserPollVote'];

    function PredictionDetailController($scope, $rootScope, $stateParams, previousState, entity, Prediction, PredictionType, UserProfile, UserPollVote) {
        var vm = this;

        vm.prediction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('predictimoApp:predictionUpdate', function(event, result) {
            vm.prediction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
