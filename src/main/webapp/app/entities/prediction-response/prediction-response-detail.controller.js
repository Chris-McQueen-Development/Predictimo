(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionResponseDetailController', PredictionResponseDetailController);

    PredictionResponseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PredictionResponse', 'UserProfile', 'Prediction'];

    function PredictionResponseDetailController($scope, $rootScope, $stateParams, previousState, entity, PredictionResponse, UserProfile, Prediction) {
        var vm = this;

        vm.predictionResponse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('predictimoApp:predictionResponseUpdate', function(event, result) {
            vm.predictionResponse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
