(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('UserProfileDialogController', UserProfileDialogController);

    UserProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserProfile', 'User', 'UserPollVote', 'Prediction'];

    function UserProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UserProfile, User, UserPollVote, Prediction) {
        var vm = this;

        vm.userProfile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.userpollvotes = UserPollVote.query();
        vm.predictions = Prediction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userProfile.id !== null) {
                UserProfile.update(vm.userProfile, onSaveSuccess, onSaveError);
            } else {
                UserProfile.save(vm.userProfile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('predictimoApp:userProfileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
