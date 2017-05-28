(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionTypeDeleteController',PredictionTypeDeleteController);

    PredictionTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'PredictionType'];

    function PredictionTypeDeleteController($uibModalInstance, entity, PredictionType) {
        var vm = this;

        vm.predictionType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PredictionType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
