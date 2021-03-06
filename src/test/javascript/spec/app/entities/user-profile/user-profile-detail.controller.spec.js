'use strict';

describe('Controller Tests', function() {

    describe('UserProfile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserProfile, MockUser, MockUserPollVote, MockPrediction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            MockUser = jasmine.createSpy('MockUser');
            MockUserPollVote = jasmine.createSpy('MockUserPollVote');
            MockPrediction = jasmine.createSpy('MockPrediction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserProfile': MockUserProfile,
                'User': MockUser,
                'UserPollVote': MockUserPollVote,
                'Prediction': MockPrediction
            };
            createController = function() {
                $injector.get('$controller')("UserProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'predictimoApp:userProfileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
