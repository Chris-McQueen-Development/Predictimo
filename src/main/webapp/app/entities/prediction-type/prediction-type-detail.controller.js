(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionTypeDetailController', PredictionTypeDetailController);

    PredictionTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PredictionType', 'Prediction'];

    function PredictionTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, PredictionType, Prediction) {
        var vm = this;

        vm.predictionType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('predictimoApp:predictionTypeUpdate', function(event, result) {
            vm.predictionType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
