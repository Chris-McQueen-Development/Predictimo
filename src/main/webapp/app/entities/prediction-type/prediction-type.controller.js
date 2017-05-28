(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionTypeController', PredictionTypeController);

    PredictionTypeController.$inject = ['PredictionType'];

    function PredictionTypeController(PredictionType) {

        var vm = this;

        vm.predictionTypes = [];

        loadAll();

        function loadAll() {
            PredictionType.query(function(result) {
                vm.predictionTypes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
