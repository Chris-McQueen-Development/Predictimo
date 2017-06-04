'use strict';

describe('Controller Tests', function() {

    describe('PredictionResponse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPredictionResponse, MockUserProfile, MockPrediction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPredictionResponse = jasmine.createSpy('MockPredictionResponse');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            MockPrediction = jasmine.createSpy('MockPrediction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PredictionResponse': MockPredictionResponse,
                'UserProfile': MockUserProfile,
                'Prediction': MockPrediction
            };
            createController = function() {
                $injector.get('$controller')("PredictionResponseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'predictimoApp:predictionResponseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
