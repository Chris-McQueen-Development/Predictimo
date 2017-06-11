(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionTypeController', PredictionTypeController);

    PredictionTypeController.$inject = ['PredictionType', 'Principal'];

    function PredictionTypeController(PredictionType, Principal) {

        var vm = this;
        
        vm.account = null;
        vm.isAdmin = false;

        vm.predictionTypes = [];
        
        Principal.identity().then(function (account) {
            vm.account = account;
           
            for (var i = 0; i < vm.account.authorities.length; i++) {
                console.log(i);
                if (vm.account.authorities[i] === "ROLE_ADMIN")
                    vm.isAdmin = true;
            }
        });

        loadAll();

        function loadAll() {
            PredictionType.query(function(result) {
                vm.predictionTypes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
