(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionResponseController', PredictionResponseController);

    PredictionResponseController.$inject = ['PredictionResponse'];

    function PredictionResponseController(PredictionResponse) {

        var vm = this;

        vm.predictionResponses = [];

        loadAll();

        function loadAll() {
            PredictionResponse.query(function(result) {
                vm.predictionResponses = result;
                vm.searchQuery = null;
            });
        }
    }
})();
