(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionController', PredictionController);

    PredictionController.$inject = ['Prediction', 'Principal'];

    function PredictionController(Prediction, Principal) {

        var vm = this;
        vm.account = null;
        vm.isAdmin = false;

        vm.predictions = [];
        
        Principal.identity().then(function (account) {
            console.log(account);
            vm.account = account;
           
            for (var i = 0; i < vm.account.authorities.length; i++) {
                if (vm.account.authorities[i] === "ROLE_ADMIN")
                    vm.isAdmin = true;
            }
        });

        loadAll();

        function loadAll() {
            Prediction.query(function(result) {
                vm.predictions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
