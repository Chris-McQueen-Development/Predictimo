(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionPollDetailController', PredictionPollDetailController);

    PredictionPollDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PredictionPoll'];

    function PredictionPollDetailController($scope, $rootScope, $stateParams, previousState, entity, PredictionPoll) {
        var vm = this;

        vm.predictionPoll = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('predictimoApp:predictionPollUpdate', function(event, result) {
            vm.predictionPoll = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
