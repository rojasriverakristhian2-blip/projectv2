import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanDesarrollo } from './plan-desarrollo';

describe('PlanDesarrollo', () => {
  let component: PlanDesarrollo;
  let fixture: ComponentFixture<PlanDesarrollo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanDesarrollo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanDesarrollo);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
