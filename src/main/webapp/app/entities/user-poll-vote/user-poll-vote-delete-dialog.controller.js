(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('UserPollVoteDeleteController',UserPollVoteDeleteController);

    UserPollVoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserPollVote'];

    function UserPollVoteDeleteController($uibModalInstance, entity, UserPollVote) {
        var vm = this;

        vm.userPollVote = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserPollVote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
