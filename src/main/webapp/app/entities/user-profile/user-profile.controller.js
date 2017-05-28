(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('UserProfileController', UserProfileController);

    UserProfileController.$inject = ['UserProfile'];

    function UserProfileController(UserProfile) {

        var vm = this;

        vm.userProfiles = [];

        loadAll();

        function loadAll() {
            UserProfile.query(function(result) {
                vm.userProfiles = result;
                vm.searchQuery = null;
            });
        }
    }
})();
