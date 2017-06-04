(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionResponseDeleteController',PredictionResponseDeleteController);

    PredictionResponseDeleteController.$inject = ['$uibModalInstance', 'entity', 'PredictionResponse'];

    function PredictionResponseDeleteController($uibModalInstance, entity, PredictionResponse) {
        var vm = this;

        vm.predictionResponse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PredictionResponse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
