(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('UserPollVoteController', UserPollVoteController);

    UserPollVoteController.$inject = ['UserPollVote'];

    function UserPollVoteController(UserPollVote) {

        var vm = this;

        vm.userPollVotes = [];

        loadAll();

        function loadAll() {
            UserPollVote.query(function(result) {
                vm.userPollVotes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
