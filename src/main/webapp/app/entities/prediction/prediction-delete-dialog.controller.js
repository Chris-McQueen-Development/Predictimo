(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionDeleteController',PredictionDeleteController);

    PredictionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prediction'];

    function PredictionDeleteController($uibModalInstance, entity, Prediction) {
        var vm = this;

        vm.prediction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prediction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
