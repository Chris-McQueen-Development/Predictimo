(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionPollController', PredictionPollController);

    PredictionPollController.$inject = ['PredictionPoll'];

    function PredictionPollController(PredictionPoll) {

        var vm = this;

        vm.predictionPolls = [];

        loadAll();

        function loadAll() {
            PredictionPoll.query(function(result) {
                vm.predictionPolls = result;
                vm.searchQuery = null;
            });
        }
    }
})();
