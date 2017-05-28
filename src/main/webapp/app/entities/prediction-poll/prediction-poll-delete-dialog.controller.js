(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionPollDeleteController',PredictionPollDeleteController);

    PredictionPollDeleteController.$inject = ['$uibModalInstance', 'entity', 'PredictionPoll'];

    function PredictionPollDeleteController($uibModalInstance, entity, PredictionPoll) {
        var vm = this;

        vm.predictionPoll = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PredictionPoll.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
