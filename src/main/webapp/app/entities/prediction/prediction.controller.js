(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionController', PredictionController);

    PredictionController.$inject = ['Prediction'];

    function PredictionController(Prediction) {

        var vm = this;

        vm.predictions = [];

        loadAll();

        function loadAll() {
            Prediction.query(function(result) {
                vm.predictions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
